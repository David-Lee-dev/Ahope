import { CallHandler, ExecutionContext, Injectable, NestInterceptor, RequestTimeoutException } from '@nestjs/common';
import { Observable, TimeoutError, catchError, throwError, timeout } from 'rxjs';
import { LoggingService } from 'src/service';

@Injectable()
export class TimeoutInterceptor implements NestInterceptor {
  constructor(private readonly logging: LoggingService) {}

  intercept(context: ExecutionContext, next: CallHandler<any>): Observable<any> {
    const now = Date.now();
    const request = context.switchToHttp().getRequest();

    return next.handle().pipe(
      timeout(3000),
      catchError(error => {
        if (error instanceof TimeoutError) {
          this.logging.error(error, request.method, request.url, Date.now() - now);
          return throwError(() => new RequestTimeoutException());
        }

        return throwError(() => error);
      }),
    );
  }
}
