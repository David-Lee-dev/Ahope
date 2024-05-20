package com.chanceToMe.MoonGuWanGu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.Card;
import com.chanceToMe.MoonGuWanGu.model.Member;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import com.chanceToMe.MoonGuWanGu.repository.CardRepository;
import com.chanceToMe.MoonGuWanGu.repository.MemberRepository;
import com.chanceToMe.MoonGuWanGu.repository.MetaDataRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    CardService cardService;

    @Mock
    CardRepository cardRepository;

    @Mock
    MetaDataRepository metaDataRepository;

    @Mock
    MemberRepository memberRepository;

    @Nested
    @DisplayName("draw")
    class DrawTest {

        Member member = new Member(UUID.randomUUID(), "test", Instant.now().toEpochMilli(), 0);
        MetaData metaData = new MetaData(UUID.randomUUID(), "test", 1, 0, 0, false, "test");

        @Test
        @DisplayName("Card 생성")
        void ideal() {
            Card card = new Card(UUID.randomUUID(), member, metaData, 1);
            when(metaDataRepository.findByIdWithLock(any(UUID.class))).thenReturn(metaData);
            when(metaDataRepository.findActive()).thenReturn(Arrays.asList(metaData));
            when(metaDataRepository.update(any(MetaData.class))).thenReturn(metaData);
            when(memberRepository.findById(any(UUID.class))).thenReturn(member);
            when(memberRepository.update(any(Member.class))).thenReturn(member);

            Card result = cardService.drawCard(member.getId());

            assertThat(result.getMember().getId()).isEqualTo(card.getMember().getId());
            assertThat(result.getMetaData().getId()).isEqualTo(card.getMetaData().getId());
        }

        @Test
        @DisplayName("존재하지 않는 Member일 경우 NON_EXISTED 예외 발생")
        void nonExistedMember() {
            when(memberRepository.findById(any(UUID.class))).thenThrow(
                EmptyResultDataAccessException.class);

            CustomException exception = catchThrowableOfType(() -> cardService.drawCard(member.getId()),
                CustomException.class);

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NON_EXISTED);
        }

        @Test
        @DisplayName("존재하지 않는 MetaData일 경우 NON_EXISTED 예외 발생")
        void nonExistedMetaData() {
            when(memberRepository.findById(any(UUID.class))).thenReturn(member);
            when(metaDataRepository.findActive()).thenReturn(Arrays.asList(metaData));
            when(metaDataRepository.findByIdWithLock(any(UUID.class))).thenThrow(
                EmptyResultDataAccessException.class);

            CustomException exception = catchThrowableOfType(() -> cardService.drawCard(member.getId()),
                CustomException.class);

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NON_EXISTED);
        }

        @Test
        @DisplayName("active값이 true인 MetaData가 없을 경우")
        void emptyMetaDataList() {
            when(metaDataRepository.findActive()).thenReturn(new ArrayList<>());
            when(memberRepository.findById(any(UUID.class))).thenReturn(member);

            CustomException exception = catchThrowableOfType(() -> cardService.drawCard(member.getId()),
                CustomException.class);

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NON_EXISTED);
        }
    }
}