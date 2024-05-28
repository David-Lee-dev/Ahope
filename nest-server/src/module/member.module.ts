import { Module } from '@nestjs/common';
import { getEntityConfig } from 'src/config';
import { MemberController } from 'src/controller';
import { MemberService } from 'src/service';

@Module({
  imports: [getEntityConfig()],
  controllers: [MemberController],
  providers: [MemberService],
})
export class MemberModule {}
