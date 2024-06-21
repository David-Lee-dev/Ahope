import {
  IsEmail,
  IsNotEmpty,
  // ValidationArguments,
  // ValidationOptions,
  // ValidatorConstraint,
  // ValidatorConstraintInterface,
  // registerDecorator,
} from 'class-validator';
import { MemberEntity } from 'src/entity';

// @ValidatorConstraint({ name: 'Match', async: false })
// export class MatchConstraint implements ValidatorConstraintInterface {
//   validate(value: any, args: ValidationArguments) {
//     const [relatedPropertyName] = args.constraints;
//     const relatedValue = (args.object as any)[relatedPropertyName];
//     return value === relatedValue;
//   }

//   defaultMessage(args: ValidationArguments) {
//     const [relatedPropertyName] = args.constraints;
//     return `$property must match ${relatedPropertyName}`;
//   }
// }

// function Match(property: string, validationOptions?: ValidationOptions) {
//   return function (object: object, propertyName: string) {
//     registerDecorator({
//       target: object.constructor,
//       propertyName: propertyName,
//       options: validationOptions,
//       constraints: [property],
//       validator: MatchConstraint,
//     });
//   };
// }

export class CreateMemberDto {
  @IsEmail()
  @IsNotEmpty()
  email: string;

  @IsNotEmpty()
  password: string;

  constructor(email: string) {
    this.email = email;
  }

  static toMemberEntity(dto: CreateMemberDto) {
    return MemberEntity.create(null, dto.email, dto.password);
  }
}
