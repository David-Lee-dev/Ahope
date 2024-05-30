import { Body, Controller, Get, Inject, Param, ParseUUIDPipe, Post, ValidationPipe } from '@nestjs/common';
import { CreateMemberDto } from 'src/dto';
import { MemberService } from 'src/service';
import { UUID } from 'crypto';

@Controller('api/member')
export class MemberController {
  constructor(private memberService: MemberService) {}

  @Post()
  async createMember(@Body() createMemberDto: CreateMemberDto) {
    return await this.memberService.saveMember(CreateMemberDto.toMemberEntity(createMemberDto));
  }

  @Get(':id')
  async getMember(@Param('id', new ParseUUIDPipe()) id: UUID) {
    return await this.memberService.findMemberById(id);
  }
}
