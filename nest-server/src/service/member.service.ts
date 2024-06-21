import { Injectable } from '@nestjs/common';
import { InjectDataSource } from '@nestjs/typeorm';
import { MemberEntity } from 'src/entity';
import { DataSource, QueryFailedError, Repository } from 'typeorm';
import { UUID } from 'crypto';
import { UserAuthenticationException, DBException, UserNotFoundException } from 'src/exception';

@Injectable()
export class MemberService {
  private memberRepository: Repository<MemberEntity>;

  constructor(@InjectDataSource() private dataSource: DataSource) {
    this.memberRepository = dataSource.getRepository(MemberEntity);
  }

  async saveMember(member: MemberEntity): Promise<MemberEntity> {
    try {
      const foundMember = await this.memberRepository.findOne({
        where: { email: member.email },
        withDeleted: true,
      });

      if (foundMember) {
        if (foundMember.deletedAt) {
          await this.memberRepository.restore({ id: foundMember.id });

          foundMember.remainTicket = 10;
          foundMember.lastGachaTimestamp = Date.now();
          foundMember.deletedAt = null;

          await this.memberRepository.save(foundMember);

          return foundMember;
        } else {
          if (member.password !== foundMember.password) {
            throw new UserAuthenticationException();
          }

          return foundMember;
        }
      } else {
        member.remainTicket = 10;
        member.lastGachaTimestamp = Date.now();

        await this.memberRepository.save(member);

        return member;
      }
    } catch (error) {
      if (error instanceof QueryFailedError) {
        throw new DBException();
      }

      throw error;
    }
  }

  async findMemberById(id: UUID): Promise<MemberEntity> {
    try {
      const foundMember = await this.memberRepository.findOne({ where: { id } });

      if (!foundMember) {
        throw new UserNotFoundException();
      }

      return foundMember;
    } catch (error) {
      if (error instanceof QueryFailedError) {
        throw new DBException();
      }

      throw error;
    }
  }

  async deleteMember(id: UUID): Promise<void> {
    try {
      const foundMember = await this.memberRepository.findOne({ where: { id } });

      if (!foundMember) {
        throw new UserNotFoundException();
      }

      await this.memberRepository.softDelete({ id });
    } catch (error) {
      if (error instanceof QueryFailedError) {
        throw new DBException();
      }

      throw error;
    }
  }
}
