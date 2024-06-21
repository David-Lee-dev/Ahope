import { Body, Controller, Delete, Get, Param, ParseUUIDPipe, Post } from '@nestjs/common';
import { CreateMemberDto, MemberDto } from 'src/dto';
import { MemberService } from 'src/service';
import { UUID } from 'crypto';
import { MemberEntity } from 'src/entity';

@Controller('api/member')
export class MemberController {
  constructor(private memberService: MemberService) {}

  @Post()
  async login(@Body() createMemberDto: CreateMemberDto) {
    const member: MemberEntity = await this.memberService.saveMember(
      CreateMemberDto.toMemberEntity(createMemberDto),
    );
    return MemberDto.fromEntity(member);
  }

  @Get(':id')
  async getMember(@Param('id', new ParseUUIDPipe()) id: UUID) {
    const member: MemberEntity = await this.memberService.findMemberById(id);

    return MemberDto.fromEntity(member);
  }

  @Delete(':id')
  async removeMember(@Param('id', new ParseUUIDPipe()) id: UUID) {
    return await this.memberService.deleteMember(id);
  }
}
