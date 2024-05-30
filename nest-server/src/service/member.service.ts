import { Injectable } from '@nestjs/common';
import { InjectDataSource } from '@nestjs/typeorm';
import { MemberEntity } from 'src/entity';
import { ServiceException } from 'src/exception';
import { DataSource, Repository } from 'typeorm';
import { UUID } from 'crypto';

@Injectable()
export class MemberService {
  private memberRepository: Repository<MemberEntity>;

  constructor(@InjectDataSource() private dataSource: DataSource) {
    if (!dataSource) return;

    this.memberRepository = dataSource.getRepository(MemberEntity);
  }

  async saveMember(member: MemberEntity): Promise<MemberEntity> {
    try {
      const foundMember = await this.memberRepository.findOne({ where: { email: member.email } });

      if (foundMember) return foundMember;

      return await this.memberRepository.save(member);
    } catch (error) {
      throw new ServiceException(error, 'cannot save member');
    }
  }

  async findMemberById(id: UUID): Promise<MemberEntity> {
    try {
      return await this.memberRepository.findOne({ where: { id } });
    } catch (error) {
      throw new ServiceException(error, 'cannot save member');
    }
  }
}
