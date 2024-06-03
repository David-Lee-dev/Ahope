import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm';
import { UUID } from 'crypto';

@Entity({ name: 'member' })
export class MemberEntity {
  @PrimaryGeneratedColumn('uuid')
  id: UUID;

  @Column({ unique: true })
  email: string;

  @Column({ name: 'last_gacha_timestamp', type: 'bigint', nullable: true })
  lastGachaTimestamp: number | null = null;

  @Column({ name: 'remain_ticket', default: 0 })
  remainTicket: number = 0;

  draw() {
    const timestamp = new Date().getTime();
    const ticket = (timestamp - this.lastGachaTimestamp) / (30 * 60 * 1000);

    this.remainTicket = Math.max(0, this.remainTicket + ticket - 1);
    this.lastGachaTimestamp = timestamp;
  }

  static create(id: UUID | null, email: string, lastGachaTimestamp: number | null = null, remainTicket: number = 0) {
    const member = new MemberEntity();
    if (id) member.id = id;
    member.email = email;
    member.lastGachaTimestamp = lastGachaTimestamp;
    member.remainTicket = remainTicket;

    return member;
  }
}
