package com.chanceToMe.MoonGuWanGu.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.chanceToMe.MoonGuWanGu.dto.CreateCardDto;
import com.chanceToMe.MoonGuWanGu.service.CardService;
import java.util.ArrayList;
import java.util.UUID;
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
class CardControllerTest {

    @InjectMocks
    CardController cardController;

    @Mock
    CardService cardService;

    @Nested
    @DisplayName("draw")
    class DrawTest {

        @Test
        @DisplayName("Card 생성 성공 시 201 응답")
        void ideal() {
            when(cardService.drawCard(any(UUID.class))).thenReturn(null);

            ResponseEntity result = cardController.draw(
                new CreateCardDto(UUID.randomUUID().toString()));

            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }
    }

    @Nested
    @DisplayName("retrieveCards")
    class RetrieveCardsTest {

        @Test
        @DisplayName("memberId에 해당하는 Card List와 count, 200 응답")
        void ideal() {
            when(cardService.retrieveCardsByMember(any(UUID.class))).thenReturn(new ArrayList<>());

            ResponseEntity result = cardController.retrieveCards(UUID.randomUUID().toString());

            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }
}