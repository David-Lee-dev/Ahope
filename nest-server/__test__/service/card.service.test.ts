import { randomUUID } from 'crypto';
import { CardEntity, MemberEntity, MetadataEntity } from 'src/entity';
import { ServiceException } from 'src/exception';
import { CardService } from 'src/service';
import { DataSource, EntityManager, QueryRunner, Repository, SelectQueryBuilder } from 'typeorm';

describe('CardService', () => {
  let dataSource: DataSource;
  let cardRepository: Repository<CardEntity>;
  let memberRepository: Repository<MemberEntity>;
  let metadataRepository: Repository<MetadataEntity>;
  let cardService: CardService;

  afterEach(() => {
    jest.clearAllMocks();
    jest.restoreAllMocks();
  });

  describe('saveCard', () => {
    let queryRunner: QueryRunner;
    let queryRunnerManger: SelectQueryBuilder<MetadataEntity> | EntityManager;

    beforeEach(() => {
      dataSource = { getRepository: jest.fn(), createQueryRunner: jest.fn() } as any;
      cardRepository = new Repository(CardEntity, null, null);
      memberRepository = new Repository(MemberEntity, null, null);
      metadataRepository = new Repository(MetadataEntity, null, null);

      jest.spyOn(dataSource, 'getRepository').mockImplementationOnce(() => cardRepository);
      jest.spyOn(dataSource, 'getRepository').mockImplementationOnce(() => memberRepository);
      jest.spyOn(dataSource, 'getRepository').mockImplementationOnce(() => metadataRepository);

      queryRunnerManger = {
        createQueryBuilder: jest.fn().mockReturnThis(),
        setLock: jest.fn().mockReturnThis(),
        where: jest.fn().mockReturnThis(),
        getOneOrFail: jest.fn(),
        save: jest.fn(),
      } as any;
      queryRunner = {
        connect: jest.fn(),
        startTransaction: jest.fn(),
        commitTransaction: jest.fn(),
        rollbackTransaction: jest.fn(),
        release: jest.fn(),
        manager: queryRunnerManger,
      } as any;

      jest.spyOn(dataSource, 'createQueryRunner').mockImplementation(() => queryRunner);

      cardService = new CardService(dataSource);
    });

    it('should return saved card', async () => {
      const testUUID = randomUUID();
      const member = MemberEntity.create(randomUUID(), 'test etmail', 'test password');
      const metaData = MetadataEntity.create(null, 'test image url', 'test category');
      const card = CardEntity.create(member, metaData);

      jest.spyOn(memberRepository, 'findOne').mockResolvedValue(member);
      jest.spyOn(metadataRepository, 'find').mockResolvedValue([metaData]);
      jest
        .spyOn(queryRunnerManger as SelectQueryBuilder<MetadataEntity>, 'getOneOrFail')
        .mockResolvedValue(metaData);
      jest.spyOn(queryRunnerManger as EntityManager, 'save').mockResolvedValue(metaData);
      jest.spyOn(memberRepository, 'save').mockResolvedValue(member);
      jest.spyOn(cardRepository, 'save').mockResolvedValue(card);

      await cardService.saveCard(testUUID);
      expect(queryRunner.commitTransaction).toHaveBeenCalled();
      expect(queryRunner.release).toHaveBeenCalled();
    });

    it('should throw ServiceException on error during find member', async () => {
      const testUUID = randomUUID();

      jest.spyOn(memberRepository, 'findOne').mockRejectedValue(new Error());

      await expect(cardService.saveCard(testUUID)).rejects.toThrow(ServiceException);
      expect(queryRunner.rollbackTransaction).toHaveBeenCalled();
      expect(queryRunner.release).toHaveBeenCalled();
    });

    it('should throw ServiceException on error during find metadata', async () => {
      const testUUID = randomUUID();
      const member = MemberEntity.create(randomUUID(), 'test etmail', 'test password');

      jest.spyOn(memberRepository, 'findOne').mockResolvedValue(member);
      jest
        .spyOn(queryRunnerManger as SelectQueryBuilder<MetadataEntity>, 'getOneOrFail')
        .mockRejectedValue(new Error());

      await expect(cardService.saveCard(testUUID)).rejects.toThrow(ServiceException);
      expect(queryRunner.rollbackTransaction).toHaveBeenCalled();
      expect(queryRunner.release).toHaveBeenCalled();
    });

    it('should throw ServiceException on error during update metadata', async () => {
      const testUUID = randomUUID();
      const member = MemberEntity.create(randomUUID(), 'test etmail', 'test password');
      const metaData = MetadataEntity.create(null, 'test image url', 'test category');

      jest.spyOn(memberRepository, 'findOne').mockResolvedValue(member);
      jest.spyOn(metadataRepository, 'find').mockResolvedValue([]);
      jest
        .spyOn(queryRunnerManger as SelectQueryBuilder<MetadataEntity>, 'getOneOrFail')
        .mockResolvedValue(metaData);
      jest.spyOn(queryRunnerManger as EntityManager, 'save').mockRejectedValue(new Error());

      await expect(cardService.saveCard(testUUID)).rejects.toThrow(ServiceException);
      expect(queryRunner.rollbackTransaction).toHaveBeenCalled();
      expect(queryRunner.release).toHaveBeenCalled();
    });

    it('should throw ServiceException on error during update metadata', async () => {
      const testUUID = randomUUID();
      const member = MemberEntity.create(randomUUID(), 'test etmail', 'test password');
      const metaData = MetadataEntity.create(null, 'test image url', 'test category');

      jest.spyOn(memberRepository, 'findOne').mockResolvedValue(member);
      jest.spyOn(metadataRepository, 'find').mockResolvedValue([metaData]);
      jest
        .spyOn(queryRunnerManger as SelectQueryBuilder<MetadataEntity>, 'getOneOrFail')
        .mockResolvedValue(metaData);
      jest.spyOn(queryRunnerManger as EntityManager, 'save').mockRejectedValue(new Error());

      await expect(cardService.saveCard(testUUID)).rejects.toThrow(ServiceException);
      expect(queryRunner.rollbackTransaction).toHaveBeenCalled();
      expect(queryRunner.release).toHaveBeenCalled();
    });

    it('should throw ServiceException on error during save member', async () => {
      const testUUID = randomUUID();
      const member = MemberEntity.create(randomUUID(), 'test etmail', 'test password');
      const metaData = MetadataEntity.create(null, 'test image url', 'test category');

      jest.spyOn(memberRepository, 'findOne').mockResolvedValue(member);
      jest
        .spyOn(queryRunnerManger as SelectQueryBuilder<MetadataEntity>, 'getOneOrFail')
        .mockResolvedValue(metaData);
      jest.spyOn(queryRunnerManger as EntityManager, 'save').mockResolvedValue(metaData);
      jest.spyOn(memberRepository, 'save').mockRejectedValue(new Error());

      await expect(cardService.saveCard(testUUID)).rejects.toThrow(ServiceException);
      expect(queryRunner.rollbackTransaction).toHaveBeenCalled();
      expect(queryRunner.release).toHaveBeenCalled();
    });

    it('should throw ServiceException on error during save card', async () => {
      const testUUID = randomUUID();
      const member = MemberEntity.create(randomUUID(), 'test etmail', 'test password');
      const metaData = MetadataEntity.create(null, 'test image url', 'test category');

      jest.spyOn(memberRepository, 'findOne').mockResolvedValue(member);
      jest
        .spyOn(queryRunnerManger as SelectQueryBuilder<MetadataEntity>, 'getOneOrFail')
        .mockResolvedValue(metaData);
      jest.spyOn(queryRunnerManger as EntityManager, 'save').mockResolvedValue(metaData);
      jest.spyOn(memberRepository, 'save').mockResolvedValue(member);
      jest.spyOn(cardRepository, 'save').mockRejectedValue(new Error());

      await expect(cardService.saveCard(testUUID)).rejects.toThrow(ServiceException);
      expect(queryRunner.rollbackTransaction).toHaveBeenCalled();
      expect(queryRunner.release).toHaveBeenCalled();
    });
  });
});
