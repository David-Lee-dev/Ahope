import { Injectable } from '@nestjs/common';
import { InjectDataSource } from '@nestjs/typeorm';
import { UUID } from 'crypto';
import { Card, Member, Metadata } from 'src/entity';
import { ServiceException } from 'src/exception';
import { DataSource, QueryRunner, Repository } from 'typeorm';

@Injectable()
export class CardService {
  private cardRepository: Repository<Card>;
  private memberRepository: Repository<Member>;
  private metadataRepository: Repository<Metadata>;

  constructor(@InjectDataSource() private dataSource: DataSource) {
    if (!dataSource) throw new Error('data source is rquired');

    this.cardRepository = dataSource.getRepository(Card);
    this.memberRepository = dataSource.getRepository(Member);
    this.metadataRepository = dataSource.getRepository(Metadata);
  }

  async saveCard(memberId: UUID): Promise<Card> {
    const queryRunner: QueryRunner = this.dataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const member: Member = await this.memberRepository.findOne({ where: { id: memberId } });
      const metadata: Metadata = await queryRunner.manager
        .createQueryBuilder(Metadata, 'metadata')
        .setLock('pessimistic_read')
        .where({ id: await this.getTargetMetadataId() })
        .getOneOrFail();

      metadata.count++;
      await queryRunner.manager.save(metadata);

      member.lastGachaTimestamp = new Date().getTime();
      member.remainTicket = Math.max(0, member.remainTicket - 1);
      await this.memberRepository.save(member);

      const card: Card = Card.create(member, metadata);
      console.log(card);
      const savedCard: Card = await this.cardRepository.save(card);

      await queryRunner.commitTransaction();

      return savedCard;
    } catch (error) {
      await queryRunner.rollbackTransaction();
      throw new ServiceException(error);
    } finally {
      await queryRunner.release();
    }
  }

  async findCardsByMember(memberId: UUID): Promise<Card[]> {
    const query = `
    SELECT 
        c.id AS id, c.seq AS seq, 
        m.id AS member, 
        md.id AS metadata, md.image_url AS image_url, md.grade AS grade, md.category AS category, md.weight AS weight
    FROM card c 
    JOIN member m ON c.member = m.id 
    JOIN metadata md ON c.metadata = md.id 
    WHERE c.member = '${memberId}';
    `;
    const cards: Card[] = await this.cardRepository.query(query);
    const categorizedCardData = await this.initializeCategorizedCardData();
    const sortedCategoryList = [];

    cards.forEach(card => this.updateCategorizedCardData(categorizedCardData, card));

    Array.from(categorizedCardData.entries())
      .sort((entry1, entry2) => entry2[0].localeCompare(entry1[0]))
      .forEach(entry => {
        const category = entry[0];
        const metaDataMapByUUID = entry[1];

        const metaDataList = Array.from(metaDataMapByUUID.values());
        metaDataList.sort((metaData1: Metadata, metaData2: Metadata) => metaData2.weight - metaData1.weight);

        metaDataList.forEach((metaDataAttributes: any) => {
          if (metaDataAttributes.cards) {
            const cards = metaDataAttributes.cards;
            cards.sort((card1, card2) => card1.seq - card2.seq);
          }
        });

        const categoryData = {
          category: category,
          metaDataList: metaDataList,
        };

        sortedCategoryList.push(categoryData);
      });

    return sortedCategoryList;
  }

  private async getTargetMetadataId() {
    const metadataList: Metadata[] = await this.metadataRepository.find({
      where: { active: true },
      order: { weight: 'DESC' },
    });

    const randomNumber: number = Math.floor(Math.random() * 100000) + 1;

    for (const metadata of metadataList) {
      if (randomNumber > metadata.weight) {
        return metadata.id;
      }
      return 'd6e094f3-56e0-46be-84e8-ae33a2b049d5';
    }
  }

  private async initializeCategorizedCardData() {
    const categorizedCardDataMap = new Map();
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
    const metadataByCategory = await this.metadataRepository.manager.query(query);
    for (const categoryData of metadataByCategory) {
      const category = categoryData.category;
      const metadataList = categoryData.list;

      const metaDataMapByUUID = new Map();
      for (const metaData of metadataList) {
        const metaDataAttributes = {
          id: metaData.id,
          imageUrl: metaData.imageUrl,
          grade: metaData.grade,
          weight: metaData.weight,
          category: metaData.category,
          cards: null,
        };

        metaDataMapByUUID.set(metaData.id, metaDataAttributes);
      }

      categorizedCardDataMap.set(category, metaDataMapByUUID);
    }

    return categorizedCardDataMap;
  }

  private updateCategorizedCardData(categorizedCardData, card) {
    const category = card.category;
    const metaDataId = card.metadata;

    if (categorizedCardData.has(category)) {
      const dataById = categorizedCardData.get(category);

      if (dataById.has(metaDataId)) {
        const metaDataAttributes = dataById.get(metaDataId);

        if (!metaDataAttributes.cards) {
          const cardsList = [];
          const cardAttributes = {
            id: card.id,
            seq: card.seq,
          };
          cardsList.push(cardAttributes);
          metaDataAttributes.cards = cardsList;
        } else {
          const cardsList = metaDataAttributes.cards;
          const cardAttributes = {
            id: card.id,
            seq: card.seq,
          };
          cardsList.push(cardAttributes);
        }
      }
    }
  }
}
