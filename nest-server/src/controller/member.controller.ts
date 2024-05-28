import { Body, Controller, Get, Inject, Param, ParseUUIDPipe, Post, ValidationPipe } from '@nestjs/common';
import { CreateMemberDto } from 'src/dto';
import { MemberService } from 'src/service';
import { UUID } from 'typeorm/driver/mongodb/bson.typings';

@Controller('api/member')
export class MemberController {
  constructor(private memberService: MemberService) {}

  @Post()
  async createMember(@Body() createMemberDto: CreateMemberDto) {
    return { data: await this.memberService.saveMember(CreateMemberDto.toMemberEntity(createMemberDto)) };
  }

  @Get(':id')
  async getMember(@Param('id', new ParseUUIDPipe()) id: UUID) {
    return { data: await this.memberService.findMemberById(id) };
  }
}
