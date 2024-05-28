import { ConfigModule } from '@nestjs/config';

export default () => ConfigModule.forRoot({ envFilePath: `.env.${process.env.NODE_ENV}`, cache: true, isGlobal: true });
