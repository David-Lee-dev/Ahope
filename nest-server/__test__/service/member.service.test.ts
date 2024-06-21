import { Test, TestingModule } from '@nestjs/testing';
import { randomUUID } from 'crypto';
import { describe } from 'node:test';
import { MemberEntity } from 'src/entity';
import { DBException } from 'src/exception';
import {
  UserAuthenticationException,
  UserNotFoundException,
} from 'src/exception/service.exception';
import { MemberService } from 'src/service';
import { DataSource, EntityManager, QueryFailedError, Repository } from 'typeorm';

describe('MemberService', () => {
  //mock
  const mockQueryFailedError = new QueryFailedError('', [], new Error());
  const repository = new Repository(MemberEntity, {} as EntityManager);

  let service: MemberService;
  let dataSource: DataSource;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        MemberService,
        {
          provide: DataSource,
          useValue: {
            getRepository: () => repository,
          },
        },
      ],
    }).compile();

    service = module.get<MemberService>(MemberService);
    dataSource = module.get<DataSource>(DataSource);
  });

  afterEach(() => {
    jest.clearAllMocks();
    jest.restoreAllMocks();
  });

  describe('saveMember', () => {
    it('should throw DBException on QueryFailedError', async () => {
      const member = MemberEntity.create(randomUUID(), 'test email', 'test password');
      jest.spyOn(repository, 'findOne').mockRejectedValue(mockQueryFailedError);

      await expect(service.saveMember(member)).rejects.toThrow(DBException);
    });

    it('should restore when user deleted', async () => {
      const member = MemberEntity.create(randomUUID(), 'test email', 'test password', 0, 0);
      member.deletedAt = new Date();

      jest.spyOn(repository, 'findOne').mockResolvedValue(member);
      jest.spyOn(repository, 'restore').mockResolvedValue(null);
      jest.spyOn(repository, 'save').mockResolvedValue(null);

      const result = await service.saveMember(member);

      expect(result.deletedAt).toEqual(null);
    });

    it('should return member as it is when user existed and not deleted', async () => {
      const member = MemberEntity.create(randomUUID(), 'test email', 'test password');

      jest.spyOn(repository, 'findOne').mockResolvedValue(member);
      jest.spyOn(repository, 'restore').mockResolvedValue(null);
      jest.spyOn(repository, 'save').mockResolvedValue(null);

      const result = await service.saveMember(member);

      expect(result).toEqual(member);
    });

    it('should throw UserAuthenticationException', async () => {
      const member = MemberEntity.create(randomUUID(), 'test email', 'test password');
      const wrongPasswordMember = { ...member, password: 'wrong password' } as any;
      jest.spyOn(repository, 'findOne').mockResolvedValue(member);

      await expect(service.saveMember(wrongPasswordMember)).rejects.toThrow(
        UserAuthenticationException,
      );
    });

    it('should save new member', async () => {
      const member = MemberEntity.create(randomUUID(), 'test email', 'test password');

      jest.spyOn(repository, 'findOne').mockResolvedValue(null);
      jest.spyOn(repository, 'save').mockResolvedValue(member);

      const result = await service.saveMember(member);
      expect(result).toEqual(member);
    });
  });

  describe('findMemberById', () => {
    it('should throw UserNotFoundException on user not found', async () => {
      const id = randomUUID();

      jest.spyOn(repository, 'findOne').mockResolvedValue(null);

      await expect(service.findMemberById(id)).rejects.toThrow(UserNotFoundException);
    });

    it('should throw DBException on QueryFailedError', async () => {
      const id = randomUUID();

      jest.spyOn(repository, 'findOne').mockRejectedValue(mockQueryFailedError);

      await expect(service.findMemberById(id)).rejects.toThrow(DBException);
    });

    it('should save new member', async () => {
      const id = randomUUID();
      const member = MemberEntity.create(id, 'test email', 'test password');

      jest.spyOn(repository, 'findOne').mockResolvedValue(member);

      const result = await service.findMemberById(id);

      expect(result).toEqual(member);
    });
  });

  describe('deleteMember', () => {
    it('should throw UserNotFoundException on user not found', async () => {
      const id = randomUUID();

      jest.spyOn(repository, 'findOne').mockResolvedValue(null);

      await expect(service.deleteMember(id)).rejects.toThrow(UserNotFoundException);
    });

    it('should throw DBException on QueryFailedError', async () => {
      const id = randomUUID();

      jest.spyOn(repository, 'findOne').mockRejectedValue(mockQueryFailedError);

      await expect(service.deleteMember(id)).rejects.toThrow(DBException);
    });

    it('should soft delete member', async () => {
      const id = randomUUID();
      const member = MemberEntity.create(id, 'test email', 'test password');

      jest.spyOn(repository, 'findOne').mockResolvedValue(member);
      jest.spyOn(repository, 'softDelete').mockResolvedValue(null);

      const result = await service.deleteMember(id);

      expect(repository.softDelete).toHaveBeenCalled();
    });
  });
});
