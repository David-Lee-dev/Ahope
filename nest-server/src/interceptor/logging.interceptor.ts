import { Injectable, NestInterceptor, ExecutionContext, CallHandler } from '@nestjs/common';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoggingService } from 'src/service';

@Injectable()
export class LoggingInterceptor implements NestInterceptor {
  constructor(private readonly logging: LoggingService) {}

  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const now = Date.now();
    const request = context.switchToHttp().getRequest();
    return next.handle().pipe(
      tap({
        next: (val: unknown): void => {
          const processingTimeMs = Date.now() - now;

          if (processingTimeMs < 1000) {
            this.logging.info(request.method, request.url, processingTimeMs);
          } else {
            this.logging.warn(request.method, request.url, processingTimeMs);
          }
        },
        error: (error: Error): void => {
          this.logging.error(error, request.method, request.url, Date.now() - now);
          throw error;
        },
      }),
    );
  }
}
