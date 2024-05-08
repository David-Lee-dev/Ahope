package com.chanceToMe.MoonGuWanGu.service;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.Card;
import com.chanceToMe.MoonGuWanGu.model.Member;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import com.chanceToMe.MoonGuWanGu.repository.CardRepository;
import com.chanceToMe.MoonGuWanGu.repository.MemberRepository;
import com.chanceToMe.MoonGuWanGu.repository.MetaDataRepository;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    MetaDataRepository metaDataRepository;

    @Autowired
    MemberRepository memberRepository;

    @Transactional
    public Card drawCard(UUID memberId) {
        try {
            Member member = memberRepository.findById(memberId);
            MetaData metaData = metaDataRepository.findByIdWithLock(getTargetMetaDataId());

            metaData.increaseCount();
            metaDataRepository.update(metaData);

            member.updateLastGachaTimestamp();
            member.decreaseRemainTicket();
            memberRepository.update(member);

            Card card = new Card(UUID.randomUUID(), member, metaData, metaData.getCount());
            cardRepository.insert(card);

            return card;
        } catch (Exception e) {
            System.out.println(e);
            if (e instanceof CustomException) {
                throw e;
            } else if (e instanceof DuplicateKeyException) {
                throw new CustomException(ErrorCode.DUPLICATED_KEY, e.getStackTrace());
            } else if (e instanceof EmptyResultDataAccessException) {
                throw new CustomException(ErrorCode.NON_EXISTED, e.getStackTrace());
            } else {
                throw new CustomException(ErrorCode.UNKNOWN, e.getStackTrace());
            }
        }
    }

    public List<Card> retrieveCardsByMember(UUID memberId) {
        return cardRepository.findByMember(memberId);
    }

    private UUID getTargetMetaDataId() {
        try {
            List<MetaData> metaDataList = metaDataRepository.findActive();
            int randomNumber = new Random().nextInt(100000) + 1;
            UUID targetUUID = metaDataList.get(0).getId();

            for (MetaData m : metaDataList) {
                if (randomNumber > m.getWeight()) {
                    targetUUID = m.getId();
                    break;
                }
            }

            return targetUUID;
        } catch (Exception e) {
            if (e instanceof IndexOutOfBoundsException) {
                throw new CustomException(ErrorCode.NON_EXISTED, e.getStackTrace());
            } else {
                System.out.println(e);
                throw new CustomException(ErrorCode.UNKNOWN, e.getStackTrace());
            }
        }
    }
}
