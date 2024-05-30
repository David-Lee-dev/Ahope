import { UUID } from 'crypto';
import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm';

@Entity()
export class MetadataEntity {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ name: 'image_url', length: 500, unique: true })
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

  increaseCount(): void {
    this.count++;
  }

  static create(
    id: UUID | null,
    imageUrl: string,
    category: string,
    count: number = 0,
    grade: number = 0,
    weight: number = 0,
    active: boolean = true,
  ): MetadataEntity {
    const metaData = new MetadataEntity();
    if (id) metaData.id = id;
    metaData.imageUrl = imageUrl;
    metaData.category = category;
    metaData.count = count;
    metaData.grade = grade;
    metaData.weight = weight;
    metaData.active = active;

    return metaData;
  }
}
