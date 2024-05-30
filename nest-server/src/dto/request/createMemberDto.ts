import { IsEmail, IsNotEmpty } from 'class-validator';
import { MemberEntity } from 'src/entity';

export class CreateMemberDto {
  @IsEmail()
  @IsNotEmpty()
  email: string;

  constructor(email: string) {
    this.email = email;
  }

  static toMemberEntity(dto: CreateMemberDto) {
    return MemberEntity.create(null, dto.email);
  }
}
