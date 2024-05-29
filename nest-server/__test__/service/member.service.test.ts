import { randomUUID } from 'crypto';
import { Member } from 'src/entity';
import { ServiceException } from 'src/exception';
import { MemberService } from 'src/service';
import { DataSource, Repository } from 'typeorm';

describe('MemberService', () => {
  let dataSource: DataSource;
  let memberRepository: Repository<Member>;
  let memberService: MemberService;

  afterEach(() => {
    jest.clearAllMocks();
    jest.restoreAllMocks();
  });

  describe('saveMember', () => {
    beforeEach(() => {
      memberRepository = new Repository(Member, null, null);
      dataSource = { getRepository: () => memberRepository } as any;

      memberService = new MemberService(dataSource);
    });

    it('should return saved Member', async () => {
      const member = Member.create(randomUUID(), 'test etmail');

      jest.spyOn(memberRepository, 'save').mockResolvedValue(member);

      const result = await memberService.saveMember(member);

      expect(result).toEqual(member);
    });

    it('should throw ServiceException on error during save member', async () => {
      const member = Member.create(randomUUID(), 'test etmail');

      jest.spyOn(memberRepository, 'save').mockRejectedValue(new Error());

      await expect(memberService.saveMember(member)).rejects.toThrow(ServiceException);
    });
  });

  describe('findMemberById', () => {
    beforeEach(() => {
      memberRepository = new Repository(Member, null, null);
      dataSource = { getRepository: () => memberRepository } as any;

      memberService = new MemberService(dataSource);
    });

    it('should return Member', async () => {
      const member = Member.create(randomUUID(), 'test etmail');

      jest.spyOn(memberRepository, 'findOne').mockResolvedValue(member);

      const result = await memberService.findMemberById(member.id);

      expect(result).toEqual(member);
    });

    it('should throw ServiceException on error during find member', async () => {
      const member = Member.create(randomUUID(), 'test etmail');

      jest.spyOn(memberRepository, 'findOne').mockRejectedValue(new Error());

      await expect(memberService.findMemberById(member.id)).rejects.toThrow(ServiceException);
    });
  });
});
