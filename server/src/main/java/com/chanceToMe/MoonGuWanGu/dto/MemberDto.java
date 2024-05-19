package com.chanceToMe.MoonGuWanGu.dto;

import com.chanceToMe.MoonGuWanGu.model.Member;
import java.util.UUID;
import lombok.Getter;

@Getter
public class MemberDto {
    UUID id;

    String email;

    Long lastGachaTimestamp;

    int remainTicket;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.lastGachaTimestamp = member.getLastGachaTimestamp();
        this.remainTicket = member.getRemainTicket();
    }
}
