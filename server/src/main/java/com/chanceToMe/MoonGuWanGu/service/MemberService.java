package com.chanceToMe.MoonGuWanGu.service;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.Member;
import com.chanceToMe.MoonGuWanGu.repository.MemberRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

  @Autowired
  private MemberRepository memberRepository;

  public Member createMember(String email) {
    try {
      Member member = Member.builder().email(email).remainTicket(3).build();

      try {
       member = memberRepository.findByEmail(email);
      } catch (Exception e) {
        if (e instanceof EmptyResultDataAccessException) {
          memberRepository.insert(member);
        } else {
          throw e;
        }
      }

      return member;
    } catch (Exception e) {
      if (e instanceof DuplicateKeyException) {
        throw new CustomException(ErrorCode.DUPLICATED_KEY, e.getStackTrace());
      } else {
        throw new CustomException(ErrorCode.UNKNOWN, e.getStackTrace());
      }
    }
  }

  public Member findMember(UUID memberId) {
    try {
      return memberRepository.findById(memberId);
    } catch (Exception e) {
      if (e instanceof EmptyResultDataAccessException) {
        throw new CustomException(ErrorCode.NON_EXISTED, e.getStackTrace());
      } else {
        throw new CustomException(ErrorCode.UNKNOWN, e.getStackTrace());
      }
    }
  }
}
