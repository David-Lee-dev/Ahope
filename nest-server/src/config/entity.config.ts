import { TypeOrmModule } from '@nestjs/typeorm';
import { Card, Member, Metadata } from 'src/entity';

export default () => TypeOrmModule.forFeature([Card, Member, Metadata]);
