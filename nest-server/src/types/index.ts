import { UUID } from 'crypto';

export type Category = string;

export type MetadataID = UUID;

export interface MetadataByCategory {
  category: string;
  list: Array<{
    id: string;
    imageUrl: string;
    grade: string;
    weight: number;
    category: string;
  }>;
}

export interface MetadataAttributes {
  id: string;
  imageUrl: string;
  grade: string;
  weight: number;
  category: string;
  cards: Card[] | null;
}

export interface Card {
  id: UUID;
  seq: number;
  member: UUID;
  metadata: UUID;
  imageUrl: string;
  grade: number;
  category: string;
  weight: number;
}
