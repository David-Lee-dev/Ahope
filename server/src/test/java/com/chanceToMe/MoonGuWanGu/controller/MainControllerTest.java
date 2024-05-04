package com.chanceToMe.MoonGuWanGu.controller;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    @InjectMocks
    MainController mainController;

    @Mock
    MemberService memberService;

    @Nested
    @DisplayName("join")
    class Join {

        String validEmail = "test@valid.com";
        String invalidEmail = "invalid email";

        @Test
        @DisplayName("계정 생성 성공 시 201 응답")
        void ideal() {
            when(memberService.createMember(validEmail)).thenReturn(any(Member.class));

            ResponseEntity result = mainController.join(new CreateMemberDto(validEmail));
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }

        @Test
        @DisplayName("이메일 형식이 맞지 않는 경우 400 응답")
        void invalidEmail() {
            memberService.createMember(invalidEmail);

            assertThatThrownBy(() -> mainController.join(new CreateMemberDto(invalidEmail))).isInstanceOf(CustomException.class);
        }

        @Test
        @DisplayName("중복된 이메일의 경우 404 응답")
        void duplicatedEmail() {
            when(memberService.createMember(validEmail)).thenThrow(new CustomException(ErrorCode.DUPLICATED_KEY));

            assertThatThrownBy(() -> mainController.join(new CreateMemberDto(validEmail))).isInstanceOf(CustomException.class);
        }


        @Test
        @DisplayName("알 수 없는 예외가 발생한 경우 500 응답")
        void unknownError() {
            when(memberService.createMember(validEmail)).thenThrow(new CustomException(ErrorCode.UNKNOWN));

            assertThatThrownBy(() -> mainController.join(new CreateMemberDto(validEmail))).isInstanceOf(CustomException.class);
        }
    }
}