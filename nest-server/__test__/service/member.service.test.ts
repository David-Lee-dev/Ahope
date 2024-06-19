import { randomUUID } from 'crypto';
import { MemberDto } from 'src/dto';
import { MemberEntity } from 'src/entity';
import { ServiceException } from 'src/exception';
import { MemberService } from 'src/service';
import { DataSource, Repository } from 'typeorm';

describe('MemberService', () => {
  let dataSource: DataSource;
  let memberRepository: Repository<MemberEntity>;
  let memberService: MemberService;

  afterEach(() => {
    jest.clearAllMocks();
    jest.restoreAllMocks();
  });

  describe('saveMember', () => {
    beforeEach(() => {
      memberRepository = new Repository(MemberEntity, null, null);
      dataSource = { getRepository: () => memberRepository } as any;

      memberService = new MemberService(dataSource);
    });

    it('should save new member', async () => {
      const member = MemberEntity.create(randomUUID(), 'test etmail', 'test password');

      jest.spyOn(memberRepository, 'findOne').mockResolvedValue(null);
      jest.spyOn(memberRepository, 'restore').mockResolvedValue(null);
      jest.spyOn(memberRepository, 'save').mockResolvedValue(member);

      const result = await memberService.saveMember(member);

      expect(result).toEqual(MemberDto.fromEntity(member));
      expect(memberRepository.restore).not.toHaveBeenCalled();
    });

    it('should restore on deleted member', async () => {
      const member = MemberEntity.create(randomUUID(), 'test etmail', 'test password');

      jest.spyOn(memberRepository, 'findOne').mockResolvedValue(member);
      jest.spyOn(memberRepository, 'restore').mockResolvedValue(null);
      jest.spyOn(memberRepository, 'save').mockResolvedValue(member);

      const result = await memberService.saveMember(member);

      expect(result).toEqual(MemberDto.fromEntity(member));
    });

    it('should throw ServiceException on error during save member', async () => {
      const member = MemberEntity.create(randomUUID(), 'test etmail', 'test password');

      jest.spyOn(memberRepository, 'save').mockRejectedValue(new Error());

      await expect(memberService.saveMember(member)).rejects.toThrow(ServiceException);
    });
  });
});
