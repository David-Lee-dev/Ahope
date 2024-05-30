import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { MemberEntity } from './member.entity';
import { MetadataEntity } from './metadata.entity';

@Entity()
export class CardEntity {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ type: 'bigint' })
  seq: number;

  @ManyToOne(() => MemberEntity)
  @JoinColumn({ name: 'member' })
  member: MemberEntity;

  @ManyToOne(() => MetadataEntity)
  @JoinColumn({ name: 'metadata' })
  metadata: MetadataEntity;

  static create(member: MemberEntity, metadata: MetadataEntity) {
    const card = new CardEntity();
    card.seq = metadata.count;
    card.member = member;
    card.metadata = metadata;

    return card;
  }
}
