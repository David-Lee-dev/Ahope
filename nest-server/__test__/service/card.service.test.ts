import { Test, TestingModule } from '@nestjs/testing';
import { randomUUID } from 'node:crypto';
import { describe } from 'node:test';
import { MemberEntity, MetadataEntity } from 'src/entity';
import {
  DBException,
  MetadataNotFoundException,
  UserNotEnoughTicketException,
  UserNotFoundException,
} from 'src/exception';
import { CardService } from 'src/service';
import { DataSource, EntityManager, QueryFailedError, QueryRunner, Repository } from 'typeorm';

describe('CardService', () => {
  let service: CardService;
  let dataSource: DataSource;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        CardService,
        {
          provide: DataSource,
          useValue: {
            getRepository: jest.fn(),
            createQueryRunner: jest.fn(),
          },
        },
      ],
    }).compile();

    service = module.get<CardService>(CardService);
    dataSource = module.get<DataSource>(DataSource);
  });

  afterEach(() => {
    jest.clearAllMocks();
    jest.restoreAllMocks();
  });

  describe('saveCard', () => {
    let manager: EntityManager;
    const queryRunner: QueryRunner = {
      connect: jest.fn(),
      startTransaction: jest.fn(),
      commitTransaction: jest.fn(),
      rollbackTransaction: jest.fn(),
      release: jest.fn(),
      manager: {
        findOne: jest.fn(),
        createQueryBuilder: jest.fn().mockReturnValue({
          setLock: jest.fn().mockReturnThis(),
          where: jest.fn().mockReturnThis(),
          getOne: jest.fn(),
        }),
        save: jest.fn(),
      } as any,
    } as any;

    beforeEach(() => {
      jest.spyOn(dataSource, 'createQueryRunner').mockImplementation(() => queryRunner);
    });

    it('should throw UserNotFoundException', async () => {
      const memberId = randomUUID();

      jest.spyOn(queryRunner.manager, 'findOne').mockResolvedValueOnce(null);

      await expect(service.saveCard(memberId)).rejects.toThrow(UserNotFoundException);
    });

    it('should throw MetadataNotFoundException', async () => {
      const memberId = randomUUID();
      const member = new MemberEntity();

      jest.spyOn(service as any, 'getTargetMetadataId').mockResolvedValue(randomUUID);
      jest.spyOn(queryRunner.manager, 'findOne').mockResolvedValueOnce(member);
      jest.spyOn(queryRunner.manager.createQueryBuilder(), 'getOne').mockResolvedValueOnce(null);

      await expect(service.saveCard(memberId)).rejects.toThrow(MetadataNotFoundException);
    });

    it('should throw UserNotEnoughTicketException', async () => {
      const memberId = randomUUID();
      const member = MemberEntity.create(
        memberId,
        'test email',
        'test password',
        new Date().getTime(),
        0,
      );

      jest.spyOn(service as any, 'getTargetMetadataId').mockResolvedValue(randomUUID);
      jest.spyOn(queryRunner.manager, 'findOne').mockResolvedValueOnce(member);
      jest
        .spyOn(queryRunner.manager.createQueryBuilder(), 'getOne')
        .mockResolvedValueOnce(new MetadataEntity());

      await expect(service.saveCard(memberId)).rejects.toThrow(UserNotEnoughTicketException);
    });

    it('should throw DBException on query failure', async () => {
      const memberId = randomUUID();
      jest
        .spyOn(queryRunner.manager, 'findOne')
        .mockRejectedValueOnce(new QueryFailedError('', [], new Error()));

      await expect(service.saveCard(memberId)).rejects.toThrow(DBException);
    });
  });
});
