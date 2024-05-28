// my-service.module.ts
import { Module } from '@nestjs/common';
import { LoggingService } from 'src/service/logging.service';

@Module({
  providers: [LoggingService],
  exports: [LoggingService],
})
export class CommonModule {}
