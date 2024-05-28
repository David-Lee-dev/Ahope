import { IsEmail, IsNotEmpty } from 'class-validator';
import { Member } from 'src/entity';

export class CreateMemberDto {
  @IsEmail()
  @IsNotEmpty()
  email: string;

  constructor(email: string) {
    this.email = email;
  }

  static toMemberEntity(dto: CreateMemberDto) {
    return Member.create(null, dto.email);
  }
}
