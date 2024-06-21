import { HttpException, HttpStatus } from '@nestjs/common';

export class UserNotFoundException extends HttpException {
  constructor() {
    super('user not found', HttpStatus.NOT_FOUND);
  }
}

export class UserAuthenticationException extends HttpException {
  constructor() {
    super('user unable to authenticate', HttpStatus.CONFLICT);
  }
}

export class UserNotEnoughTicketException extends HttpException {
  constructor() {
    super('user unable to authenticate', HttpStatus.CONFLICT);
  }
}

export class MetadataNotFoundException extends HttpException {
  constructor() {
    super('metadata not found', HttpStatus.NOT_FOUND);
  }
}

export class DBException extends HttpException {
  constructor() {
    super('internal server error', HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
