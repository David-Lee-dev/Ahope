import { WinstonModule } from 'nest-winston';
import * as winston from 'winston';

export default () =>
  WinstonModule.forRoot({
    transports: [
      new winston.transports.Console({
        level: process.env.NODE_ENV === 'production' ? 'info' : 'silly',
        format: winston.format.combine(
          winston.format.colorize({
            all: true,
          }),
          winston.format.label({
            label: '[LOGGER]',
          }),
          winston.format.timestamp({
            format: 'YYYY-MM-DD HH:mm:ss',
          }),
          winston.format.printf(info => `${info.timestamp} ${info.message}`),
        ),
      }),
    ],
  });
