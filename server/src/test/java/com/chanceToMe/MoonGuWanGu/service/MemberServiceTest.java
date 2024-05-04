package com.chanceToMe.MoonGuWanGu.service;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.Member;
import com.chanceToMe.MoonGuWanGu.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Nested
    @DisplayName("createMember")
    class CreateMemberTest {

        @Test
        @DisplayName("중복된 이메일이 아니면 Member 생성 후 생성된 Member 반환")
        void ideal() {
            Member memberCreated = Member.builder().id(UUID.randomUUID()).email("test").build();
            when(memberRepository.insert(any(Member.class))).thenReturn(memberCreated);

            Member result = memberService.createMember("test");

            assertThat(result.getEmail()).isEqualTo(memberCreated.getEmail());
            assertThat(result.getRemainTicket()).isEqualTo(0);
        }

        @Test
        @DisplayName("중복된 이메일일 경우 DUPLICATED_KEY 예외 발생")
        void duplicatedEmail() {
            when(memberRepository.insert(any(Member.class))).thenThrow(DuplicateKeyException.class);

            CustomException exception = catchThrowableOfType(() -> memberService.createMember(anyString()), CustomException.class);
            assertThat(exception).isInstanceOf(CustomException.class);
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_KEY);
        }

        @Test
        @DisplayName("처리하지 않은 예외가 발생한 경우 UNKWON 예 발생")
        void unknown() {
            when(memberRepository.insert(any(Member.class))).thenThrow(RuntimeException.class);

            CustomException exception = catchThrowableOfType(() -> memberService.createMember(anyString()), CustomException.class);
            assertThat(exception).isInstanceOf(CustomException.class);
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.UNKNOWN);
        }
    }
}