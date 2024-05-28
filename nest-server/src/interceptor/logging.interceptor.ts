import { Injectable, NestInterceptor, ExecutionContext, CallHandler } from '@nestjs/common';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoggingService } from 'src/service';

@Injectable()
export class LoggingInterceptor implements NestInterceptor {
  constructor(private readonly logging: LoggingService) {}

  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const now = Date.now();
    const requset = context.switchToHttp().getRequest();

    return next.handle().pipe(
      tap({
        next: (val: unknown): void => {
          const processingTimeMs = Date.now() - now;

          if (processingTimeMs < 1000) {
            this.logging.info(requset.method, requset.url, processingTimeMs);
          } else {
            this.logging.warn(requset.method, requset.url, processingTimeMs);
          }
        },
        error: (error: Error): void => {
          this.logging.error(error, requset.method, requset.url, Date.now() - now);
        },
      }),
    );
  }
}
