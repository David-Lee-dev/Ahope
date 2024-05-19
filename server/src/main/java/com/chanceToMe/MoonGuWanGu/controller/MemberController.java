package com.chanceToMe.MoonGuWanGu.controller;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.dto.CreateMemberDto;
import com.chanceToMe.MoonGuWanGu.dto.MemberDto;
import com.chanceToMe.MoonGuWanGu.model.Member;
import com.chanceToMe.MoonGuWanGu.service.MemberService;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {

  @Autowired
  private MemberService memberService;

  @PostMapping
  public ResponseEntity join(@RequestBody CreateMemberDto dto) {

    if (!isValidEmail(dto.getEmail())) {
      throw new CustomException(ErrorCode.INVALID, null);
    }

    Member member = memberService.createMember(dto.getEmail());

    return ResponseEntity.status(HttpStatus.CREATED).body(new MemberDto(member));
  }

  @GetMapping("/{memberId}")
  public ResponseEntity getMemberData(@PathVariable("memberId") String memberId) {
    Member member = memberService.findMember(UUID.fromString(memberId));

    return ResponseEntity.status(HttpStatus.OK).body(member);
  }

  private boolean isValidEmail(String email) {
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }
}
