package com.chanceToMe.MoonGuWanGu.dto;

import com.chanceToMe.MoonGuWanGu.model.Card;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CardDto {

    UUID id;

    String imageUrl;

    Integer grade;

    String category;

    Integer seq;

    UUID owner;

    public CardDto(Card card) {
        this.id = card.getId();
        this.imageUrl = card.getMetaData().getImageUrl();
        this.grade = card.getMetaData().getGrade();
        this.category = card.getMetaData().getCategory();
        this.seq = card.getSeq();
        this.owner = card.getMember().getId();
    }
}
