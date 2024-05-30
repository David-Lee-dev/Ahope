import { TypeOrmModule } from '@nestjs/typeorm';
import { CardEntity, MemberEntity, MetadataEntity } from 'src/entity';

export default () => TypeOrmModule.forFeature([CardEntity, MemberEntity, MetadataEntity]);
