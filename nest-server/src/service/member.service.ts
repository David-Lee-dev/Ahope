import { Injectable } from '@nestjs/common';
import { InjectDataSource } from '@nestjs/typeorm';
import { MemberEntity } from 'src/entity';
import { ServiceException } from 'src/exception';
import { DataSource, Repository } from 'typeorm';
import { UUID } from 'crypto';
import { MemberDto } from 'src/dto';

@Injectable()
export class MemberService {
  private memberRepository: Repository<MemberEntity>;

  constructor(@InjectDataSource() private dataSource: DataSource) {
    if (!dataSource) return;

    this.memberRepository = dataSource.getRepository(MemberEntity);
  }

  async saveMember(member: MemberEntity): Promise<MemberDto> {
    try {
      const foundMember = await this.memberRepository.findOne({
        where: { email: member.email },
        withDeleted: true,
      });

      if (foundMember) {
        await this.memberRepository.restore({ id: foundMember.id });

        foundMember.remainTicket = 10;
        foundMember.lastGachaTimestamp = Date.now();
        foundMember.deletedAt = null;

        await this.memberRepository.save(foundMember);

        return MemberDto.fromEntity(foundMember);
      } else {
        member.remainTicket = 10;
        member.lastGachaTimestamp = Date.now();

        await this.memberRepository.save(member);
        return MemberDto.fromEntity(member);
      }
    } catch (error) {
      throw new ServiceException(error, 'cannot save member');
    }
  }

  async findMemberById(id: UUID): Promise<MemberDto> {
    try {
      const member = await this.memberRepository.findOne({ where: { id } });
      return MemberDto.fromEntity(member);
    } catch (error) {
      throw new ServiceException(error, 'cannot find member');
    }
  }

  async deleteMember(id: UUID): Promise<void> {
    try {
      await this.memberRepository.softDelete({ id });
    } catch (error) {
      throw new ServiceException(error, 'cannot delete member');
    }
  }
}
