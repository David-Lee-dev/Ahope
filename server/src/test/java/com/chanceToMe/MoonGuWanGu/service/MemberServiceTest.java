package com.chanceToMe.MoonGuWanGu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.Member;
import com.chanceToMe.MoonGuWanGu.repository.MemberRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

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
            Member memberCreated = Member.builder().id(UUID.randomUUID()).email("test").remainTicket(3).build();
            when(memberRepository.findByEmail(anyString())).thenThrow(
                EmptyResultDataAccessException.class);
            when(memberRepository.insert(any(Member.class))).thenReturn(memberCreated);

            Member result = memberService.createMember("test");

            assertThat(result.getEmail()).isEqualTo(memberCreated.getEmail());
            assertThat(result.getRemainTicket()).isEqualTo(3);
        }

        @Test
        @DisplayName("중복된 이메일일 경우 존재하는 email에 대한 Member 반환")
        void duplicatedEmail() {
            Member existedMember = Member.builder().id(UUID.randomUUID()).email("test").remainTicket(3).build();
            when(memberRepository.findByEmail(anyString())).thenReturn(existedMember);

            Member result = memberService.createMember("test");

            assertThat(result.getEmail()).isEqualTo(existedMember.getEmail());
            assertThat(result.getRemainTicket()).isEqualTo(3);
        }

        @Test
        @DisplayName("insert 과정에서 email 중복이 발생한 경우 DUPLICATED_KEY 예외 발생")
        void duplicatedEmailWhenInsert() {
            when(memberRepository.findByEmail(anyString())).thenThrow(
                EmptyResultDataAccessException.class);
            when(memberRepository.insert(any(Member.class))).thenThrow(DuplicateKeyException.class);

            CustomException exception = catchThrowableOfType(
                () -> memberService.createMember(anyString()), CustomException.class);
            assertThat(exception).isInstanceOf(CustomException.class);
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_KEY);
        }

        @Test
        @DisplayName("처리하지 않은 예외가 발생한 경우 UNKNOWN 예 발생")
        void unknown() {
            when(memberRepository.findByEmail(anyString())).thenThrow(
                EmptyResultDataAccessException.class);
            when(memberRepository.insert(any(Member.class))).thenThrow(RuntimeException.class);

            CustomException exception = catchThrowableOfType(
                () -> memberService.createMember(anyString()), CustomException.class);
            assertThat(exception).isInstanceOf(CustomException.class);
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.UNKNOWN);
        }
    }

    @Nested
    @DisplayName("findMember")
    class FindMemberTest {

        @Test
        @DisplayName("Member 반환")
        void ideal() {
            Member member = Member.builder().id(UUID.randomUUID()).email("test").build();

            when(memberRepository.findById(any(UUID.class))).thenReturn(member);

            Member result = memberService.findMember(member.getId());

            assertThat(result.getId()).isEqualTo(member.getId());
            assertThat(result.getEmail()).isEqualTo(member.getEmail());
        }

        @Test
        @DisplayName("존재하지 않는 Member일 경우 NON_EXISTED 예외 발생")
        void nonExistedMember() {
            when(memberRepository.findById(any(UUID.class))).thenThrow(
                EmptyResultDataAccessException.class);

            CustomException exception = catchThrowableOfType(
                () -> memberService.findMember(UUID.randomUUID()), CustomException.class);

            assertThat(exception).isInstanceOf(CustomException.class);
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NON_EXISTED);
        }
    }
}