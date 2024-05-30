import { randomUUID } from 'crypto';
import { MemberController } from 'src/controller';
import { CreateMemberDto } from 'src/dto';
import { MemberEntity } from 'src/entity';
import { MemberService } from 'src/service';
import { DataSource } from 'typeorm';

describe('MemberController', () => {
  let memberService: MemberService;
  let memberController: MemberController;

  beforeEach(() => {
    memberService = new MemberService(null);
    memberController = new MemberController(memberService);
  });

  afterEach(() => {
    jest.clearAllMocks();
    jest.restoreAllMocks();
  });

  describe('createMember', () => {
    it('should return saved member', async () => {
      const testUUID = randomUUID() as any;
      const member = MemberEntity.create(testUUID, 'test email');
      const dto = new CreateMemberDto('test email');

      jest.spyOn(memberService, 'saveMember').mockResolvedValue(member);

      const result = await memberController.createMember(dto);
      expect(result).toEqual({ data: member });
    });
  });

  describe('getMember', () => {
    it('should return member', async () => {
      const testUUID = randomUUID() as any;
      const member = MemberEntity.create(testUUID, 'test email');

      jest.spyOn(memberService, 'findMemberById').mockResolvedValue(member);

      const result = await memberController.getMember(testUUID);
      expect(result).toEqual({ data: member });
    });
  });
});
