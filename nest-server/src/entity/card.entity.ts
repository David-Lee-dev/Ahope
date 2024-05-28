import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { Member } from './member.entity';
import { Metadata } from './metadata.entity';

@Entity()
export class Card {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ type: 'bigint' })
  seq: number;

  @ManyToOne(() => Member)
  @JoinColumn({ name: 'member' })
  member: Member;

  @ManyToOne(() => Metadata)
  @JoinColumn({ name: 'metadata' })
  metadata: Metadata;
}
