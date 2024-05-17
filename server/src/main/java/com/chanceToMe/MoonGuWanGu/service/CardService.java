package com.chanceToMe.MoonGuWanGu.service;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.Card;
import com.chanceToMe.MoonGuWanGu.model.Member;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import com.chanceToMe.MoonGuWanGu.repository.CardRepository;
import com.chanceToMe.MoonGuWanGu.repository.MemberRepository;
import com.chanceToMe.MoonGuWanGu.repository.MetaDataRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
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

    public Map<String, Map<UUID, Object>> retrieveCardsByMember(UUID memberId) {
        Map<String, Map<UUID, Object>> categorizedCardData = initializeCategorizedCardData();
        cardRepository.findByMember(memberId)
                      .forEach(card -> updateCategorizedCardData(categorizedCardData, card));

        return categorizedCardData;
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

    private Map<String, Map<UUID, Object>> initializeCategorizedCardData() {
        Map<String, Map<UUID, Object>> categorizedCardData = new HashMap<>();

        metaDataRepository.getMetadataListByCategory().forEach(data -> {
            Map<UUID, Object> dataByUUID = new HashMap<>();
            ((List<UUID>) data.get("idList")).forEach(id -> dataByUUID.put(id, null));
            categorizedCardData.put((String) data.get("category"), dataByUUID);
        });

        return categorizedCardData;
    }

    private void updateCategorizedCardData(Map<String, Map<UUID, Object>> categorizedCardData,
        Card card) {
        String category = card.getMetaData().getCategory();
        UUID cardId = card.getMetaData().getId();

        if (categorizedCardData.containsKey(category)) {
            Map<UUID, Object> dataById = categorizedCardData.get(category);

            if (dataById.containsKey(cardId)) {
                Map<String, Object> cardData = (Map<String, Object>) dataById.get(cardId);

                if (cardData == null) {
                    cardData = new HashMap<>();
                    cardData.put("imageUrl", card.getMetaData().getImageUrl());
                    cardData.put("grade", card.getMetaData().getGrade());
                    cardData.put("weight", card.getMetaData().getWeight());
                    cardData.put("count", 1);
                    cardData.put("cards", new ArrayList<>(Collections.singletonList(card.getId())));
                    dataById.put(cardId, cardData);
                } else {
                    cardData.put("count", (Integer) cardData.get("count") + 1);
                    List<UUID> cards = (List<UUID>) cardData.get("cards");
                    cards.add(card.getId());
                }
            }
        }
    }
}
