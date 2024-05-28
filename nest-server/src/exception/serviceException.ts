import { HttpException, HttpStatus } from '@nestjs/common';
import { PostgresErrorCode } from './errorCode.enum';

export class ServiceException extends HttpException {
  query: string;
  responseMessage: string;
  loggingMessage: string;

  constructor(error: Error, message?: string, query?: string) {
    switch ((error as any).code) {
      case PostgresErrorCode.UNIQUE_VIOLATION:
      case PostgresErrorCode.FOREIGN_KEY_VIOLATION:
      case PostgresErrorCode.NOT_NULL_VIOLATION:
        super(error.message ?? message, HttpStatus.CONFLICT);
        break;
      default:
        super(error.message ?? message, HttpStatus.INTERNAL_SERVER_ERROR);
        break;
    }

    this.query = query ?? null;

    if (Error.captureStackTrace) {
      Error.captureStackTrace(this, this.constructor);
    }
  }
}
