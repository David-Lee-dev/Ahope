import { Injectable } from '@nestjs/common';
import { InjectDataSource } from '@nestjs/typeorm';
import { UUID } from 'crypto';
import { CardEntity, MemberEntity, MetadataEntity } from 'src/entity';
import { ServiceException } from 'src/exception';
import { Card, Category, MetadataAttributes, MetadataByCategory, MetadataID } from 'src/types';
import { DataSource, QueryRunner, Repository } from 'typeorm';

@Injectable()
export class CardService {
  private cardRepository: Repository<CardEntity>;
  private memberRepository: Repository<MemberEntity>;
  private metadataRepository: Repository<MetadataEntity>;

  constructor(@InjectDataSource() private dataSource: DataSource) {
    if (!dataSource) throw new Error('data source is rquired');

    this.cardRepository = dataSource.getRepository(CardEntity);
    this.memberRepository = dataSource.getRepository(MemberEntity);
    this.metadataRepository = dataSource.getRepository(MetadataEntity);
  }

  async saveCard(memberId: UUID): Promise<CardEntity> {
    const queryRunner: QueryRunner = this.dataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const member: MemberEntity = await this.memberRepository.findOne({ where: { id: memberId } });
      const metadata: MetadataEntity = await queryRunner.manager
        .createQueryBuilder(MetadataEntity, 'metadata')
        .setLock('pessimistic_read')
        .where({ id: await this.getTargetMetadataId() })
        .getOneOrFail();

      member.draw();
      metadata.increaseCount();
      const card: CardEntity = CardEntity.create(member, metadata);

      await queryRunner.manager.save(metadata);
      await this.memberRepository.save(member);
      await this.cardRepository.save(card);
      await queryRunner.commitTransaction();

      return card;
    } catch (error) {
      await queryRunner.rollbackTransaction();
      throw new ServiceException(error);
    } finally {
      await queryRunner.release();
    }
  }

  async findCards(memberId: UUID): Promise<CardEntity[]> {
    const categorizedCardMap: Map<
      Category,
      Map<MetadataID, MetadataAttributes>
    > = await this.initializeCategorizedCardMap();
    (await this.findCardsByMember(memberId)).forEach(card => this.updateCategorizedCardMap(categorizedCardMap, card));

    const sortedCategoryList = [];
    Array.from(categorizedCardMap.entries())
      .sort(
        (
          entry1: [Category, Map<MetadataID, MetadataAttributes>],
          entry2: [Category, Map<MetadataID, MetadataAttributes>],
        ) => entry1[0].localeCompare(entry2[0]),
      )
      .forEach(entry => {
        const category = entry[0];
        const metadataMap = entry[1];
        const metadataList = Array.from(metadataMap.values());

        metadataList
          .sort((metadata1: MetadataAttributes, metadata2: MetadataAttributes) => metadata2.weight - metadata1.weight)
          .forEach((metaDataAttributes: MetadataAttributes) => {
            if (metaDataAttributes.cards) {
              metaDataAttributes.cards.sort((card1, card2) => card1.seq - card2.seq);
            }
          });

        sortedCategoryList.push({
          category: category,
          metadataList: metadataList,
        });
      });

    return sortedCategoryList;
  }

  private async findCardsByMember(memberId: UUID): Promise<Card[]> {
    const query = `
    SELECT 
        c.id AS id, 
        c.seq AS seq, 
        m.id AS member, 
        md.id AS metadata, 
        md.image_url AS imageUrl, 
        md.grade AS grade, 
        md.category AS category, 
        md.weight AS weight
    FROM card c 
    JOIN member m ON c.member = m.id 
    JOIN metadata md ON c.metadata = md.id 
    WHERE c.member = '${memberId}';
    `;

    return await this.cardRepository.query(query);
  }

  private async findMetadatasByCategory(): Promise<MetadataByCategory[]> {
    const query = `
    SELECT
        category,
        array_agg(
            json_build_object(
                'id', id,
                'imageUrl', image_url,
                'grade', grade,
                'count', count,
                'weight', weight,
                'category', category,
                'active', active
            )
        ) AS list
    FROM (
        SELECT
            *
        FROM
            metadata m
        GROUP BY
            m.category, m.id, m.image_url, m.grade, m.count, m.weight
    ) subquery
    GROUP BY
        category;
    `;

    return await this.metadataRepository.query(query);
  }

  private async getTargetMetadataId(): Promise<MetadataID> {
    const metadataList: MetadataEntity[] = await this.metadataRepository.find({
      where: { active: true },
      order: { weight: 'DESC' },
    });

    const randomNumber: number = Math.floor(Math.random() * 100000) + 1;

    for (const metadata of metadataList) {
      if (randomNumber > metadata.weight) {
        return metadata.id as UUID;
      }
    }

    return metadataList[0].id as UUID;
  }

  private async initializeCategorizedCardMap(): Promise<Map<Category, Map<MetadataID, MetadataAttributes>>> {
    const categorizedCardMap = new Map<Category, Map<UUID, MetadataAttributes>>();
    const metadataByCategory: MetadataByCategory[] = await this.findMetadatasByCategory();

    metadataByCategory.forEach((metadata: MetadataByCategory) => {
      const metadataMapByUUID = new Map<UUID, MetadataAttributes>();

      metadata.list.forEach(data => {
        const metaDataAttributes: MetadataAttributes = {
          id: data.id,
          imageUrl: data.imageUrl,
          grade: data.grade,
          weight: data.weight,
          category: data.category,
          cards: null,
        };

        metadataMapByUUID.set(data.id as UUID, metaDataAttributes);
      });

      categorizedCardMap.set(metadata.category, metadataMapByUUID);
    });

    return categorizedCardMap;
  }

  private updateCategorizedCardMap(
    categorizedCardMap: Map<Category, Map<MetadataID, MetadataAttributes>>,
    card: Card,
  ): void {
    const category = card.category;
    const metadataId = card.metadata;

    if (categorizedCardMap.has(category)) {
      const cardsByMetadataOfCategory: Map<MetadataID, MetadataAttributes> = categorizedCardMap.get(category);

      if (cardsByMetadataOfCategory.has(metadataId)) {
        const metaDataAttributes: MetadataAttributes = cardsByMetadataOfCategory.get(metadataId);
        const sanitizedCard = {
          id: card.id,
          seq: card.seq,
        } as Card;

        if (!metaDataAttributes.cards) {
          metaDataAttributes.cards = [sanitizedCard];
        } else {
          metaDataAttributes.cards.push(sanitizedCard);
        }
      }
    }
  }
}
