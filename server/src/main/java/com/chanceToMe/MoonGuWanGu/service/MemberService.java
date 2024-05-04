package com.chanceToMe.MoonGuWanGu.service;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.Member;
import com.chanceToMe.MoonGuWanGu.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member createMember(String email) {
        Member member = Member.builder()
                .id(UUID.randomUUID())
                .email(email)
                .remainTicket(0)
                .build();

        try {
            memberRepository.insert(member);

            return member;
        } catch (Exception e) {
            if(e instanceof DuplicateKeyException) {
                throw new CustomException(ErrorCode.DUPLICATED_KEY);
            } else {
                throw new CustomException(ErrorCode.UNKNOWN);
            }
        }
    }
}
