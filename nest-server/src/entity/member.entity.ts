import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm';
import { UUID } from 'crypto';

@Entity()
export class Member {
  @PrimaryGeneratedColumn('uuid')
  id: UUID;

  @Column({ unique: true })
  email: string;

  @Column({ name: 'last_gacha_timestamp', type: 'bigint', nullable: true })
  lastGachaTimestamp: number | null = null;

  @Column({ name: 'remain_ticket', default: 0 })
  remainTicket: number = 0;

  static create(id: UUID | null, email: string, lastGachaTimestamp: number | null = null, remainTicket: number = 0) {
    const member = new Member();
    if (id) member.id = id;
    member.email = email;
    member.lastGachaTimestamp = lastGachaTimestamp;
    member.remainTicket = remainTicket;

    return member;
  }
}
