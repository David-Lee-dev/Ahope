package com.chanceToMe.MoonGuWanGu.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.chanceToMe.MoonGuWanGu.dto.CreateMemberDto;
import com.chanceToMe.MoonGuWanGu.model.Member;
import com.chanceToMe.MoonGuWanGu.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

  @InjectMocks
  MemberController memberController;

  @Mock
  MemberService memberService;

  @Nested
  @DisplayName("join")
  class Join {

    String validEmail = "test@valid.com";

    @Test
    @DisplayName("Member 생성 성공 시 201 응답")
    void ideal() {
      when(memberService.createMember(validEmail)).thenReturn(any(Member.class));

      ResponseEntity result = memberController.join(new CreateMemberDto(validEmail));
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
  }
}