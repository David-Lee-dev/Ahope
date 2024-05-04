package com.chanceToMe.MoonGuWanGu.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Getter
public class Member {

    @Id
    UUID id;

    String email;

    @Nullable
    Long lastGachaTimestamp;

    @Nullable
    int remainTicket;

    @Builder
    public Member(UUID id, String email, Long lastGachaTimestamp, int remainTicket) {
        this.id = id;
        this.email = email;
        this.lastGachaTimestamp = lastGachaTimestamp;
        this.remainTicket = remainTicket;
    }
}
