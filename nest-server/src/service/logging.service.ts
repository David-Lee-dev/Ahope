// my-service.service.ts
import { Inject, Injectable } from '@nestjs/common';
import { WINSTON_MODULE_PROVIDER } from 'nest-winston';
import { Logger } from 'winston';

@Injectable()
export class LoggingService {
  constructor(@Inject(WINSTON_MODULE_PROVIDER) private readonly logger: Logger) {}

  error(error: any, method: string, endpoint: string, processingTimeMs: number) {
    this.logger.error(`[ERROR] ERROR ${error.message} ${method} ${endpoint} | ${processingTimeMs}ms`);
    this.logger.error(error.stack);
  }

  warn(method: string, endpoint: string, processingTimeMs: number) {
    this.logger.warn(`[WARN] Request processed slowly ${method} ${endpoint} | ${processingTimeMs}ms`);
  }

  info(method: string, endpoint: string, processingTimeMs: number) {
    this.logger.info(`[INFO] Request processed successfully ${method} ${endpoint} | ${processingTimeMs}ms`);
  }
}
