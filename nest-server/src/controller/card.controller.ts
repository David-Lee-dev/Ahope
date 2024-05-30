import { Controller, Get, Param, ParseUUIDPipe, Post } from '@nestjs/common';
import { UUID } from 'crypto';
import { CardService } from 'src/service';

@Controller('api')
export class CardController {
  constructor(private cardService: CardService) {}
  @Post('member/:id/card')
  async drawCard(@Param('id', new ParseUUIDPipe()) memberId: UUID) {
    return await this.cardService.saveCard(memberId);
  }

  @Get('member/:id/card')
  async retrieveCards(@Param('id', new ParseUUIDPipe()) memberId: UUID) {
    return await this.cardService.findCards(memberId);
  }
}
