import { Module } from '@nestjs/common';
import { CardService } from '../service';
import { CardController } from 'src/controller';

@Module({
  controllers: [CardController],
  providers: [CardService],
  exports: [],
})
export class CardModule {}
