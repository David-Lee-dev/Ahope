import { Module } from '@nestjs/common';
import { CommonModule, MemberModule } from './module';
import { getDbConfig, getEnvConfig, getLoggerConfig } from './config';
import { InjectDataSource } from '@nestjs/typeorm';
import { DataSource } from 'typeorm';
import { APP_INTERCEPTOR } from '@nestjs/core';
import { LoggingInterceptor, TimeoutInterceptor } from './interceptor';

@Module({
  imports: [getLoggerConfig(), getEnvConfig(), getDbConfig(), CommonModule, MemberModule],
  controllers: [],
  providers: [
    {
      provide: APP_INTERCEPTOR,
      useClass: TimeoutInterceptor,
    },
    {
      provide: APP_INTERCEPTOR,
      useClass: LoggingInterceptor,
    },
  ],
})
export class AppModule {
  constructor(@InjectDataSource() private dataSource: DataSource) {}
}
