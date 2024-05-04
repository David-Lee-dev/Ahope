package com.chanceToMe.MoonGuWanGu.model;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class Card {

    @Id
    UUID id;

    Member member;

    MetaData metaData;

    Long seq;
}
