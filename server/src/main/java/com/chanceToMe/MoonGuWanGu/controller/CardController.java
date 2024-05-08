package com.chanceToMe.MoonGuWanGu.controller;

import com.chanceToMe.MoonGuWanGu.dto.CardDto;
import com.chanceToMe.MoonGuWanGu.dto.CreateCardDto;
import com.chanceToMe.MoonGuWanGu.service.CardService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/card")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping
    ResponseEntity draw(@RequestBody @Valid CreateCardDto dto) {

        cardService.drawCard(UUID.fromString(dto.getMemberId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping
    ResponseEntity retrieveCards(@RequestParam(value = "memberId") String memberId) {

        List<CardDto> cardList = cardService.retrieveCardsByMember(UUID.fromString(memberId))
                                            .stream().map(CardDto::new).toList();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("list", cardList);
        responseBody.put("count", cardList.size());

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
