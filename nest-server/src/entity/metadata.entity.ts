import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm';

@Entity()
export class Metadata {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ length: 500, unique: true })
  imageUrl: string;

  @Column({ default: 0 })
  count: number;

  @Column({ default: 0 })
  grade: number;

  @Column()
  weight: number;

  @Column({ default: false })
  active: boolean;

  @Column({ length: 255 })
  category: string;
}
