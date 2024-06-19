import { UUID } from 'crypto';
import { MemberEntity } from 'src/entity';

export class MemberDto {
  id: UUID;

  email: string;

  lastGachaTimestamp: number | null = null;

  remainTicket: number = 0;

  constructor(id: UUID, email: string, lastGachaTimestamp: number, remainTicket: number) {
    this.id = id;
    this.email = email;
    this.lastGachaTimestamp = lastGachaTimestamp;
    this.remainTicket = remainTicket;
  }

  static fromEntity(member: MemberEntity) {
    return new MemberDto(member.id, member.email, member.lastGachaTimestamp, member.remainTicket);
  }
}
