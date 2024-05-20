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
import java.util.Comparator;
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

    public List<Map<String, Object>> retrieveCardsByMember(UUID memberId) {
        Map<String, Map<UUID, Object>> categorizedCardData = initializeCategorizedCardData();
        cardRepository.findByMember(memberId)
                      .forEach(card -> updateCategorizedCardData(categorizedCardData, card));

        List<Map<String, Object>> sortedCategoryList = new ArrayList<>();

        categorizedCardData.entrySet().stream()
                           .sorted((entry1, entry2) -> entry2.getKey().compareTo(entry1.getKey()))
                           .forEachOrdered(entry -> {
                               String category = entry.getKey();
                               Map<UUID, Object> metaDataMapByUUID = entry.getValue();

                               List<Map<String, Object>> metaDataList = new ArrayList<>();
                               for (Object value : metaDataMapByUUID.values()) {
                                   metaDataList.add((Map<String, Object>) value);
                               }
                               metaDataList.sort(Comparator.comparing(metaData -> (Integer) ((Map<String, Object>) metaData).get("weight")).reversed());

                               metaDataList.forEach(metaDataAttributes -> {
                                   if (metaDataAttributes.get("cards") != null) {
                                       List<Map<String, Object>> cards = (List<Map<String, Object>>) metaDataAttributes.get("cards");
                                       cards.sort(Comparator.comparing(card -> (Integer) card.get("seq")));
                                   }
                               });

                               Map<String, Object> categoryData = new HashMap<>();
                               categoryData.put("category", category);
                               categoryData.put("metaDataList", metaDataList);
                               sortedCategoryList.add(categoryData);
                           });

        return sortedCategoryList;
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
        Map<String, Map<UUID, Object>> categorizedCardDataMap = new HashMap<>();

        List<Map<String, Object>> metadataByCategory = metaDataRepository.getMetadataListByCategory();

        for (Map<String, Object> categoryData : metadataByCategory) {
            String category = (String) categoryData.get("category");
            List<MetaData> metaDataList = (List<MetaData>) categoryData.get("metaDataList");

            Map<UUID, Object> metaDataMapByUUID = new HashMap<>();
            for (MetaData metaData : metaDataList) {
                Map<String, Object> metaDataAttributes = new HashMap<>();
                metaDataAttributes.put("id", metaData.getId());
                metaDataAttributes.put("imageUrl", metaData.getImageUrl());
                metaDataAttributes.put("grade", metaData.getGrade());
                metaDataAttributes.put("weight", metaData.getWeight());
                metaDataAttributes.put("category", metaData.getCategory());
                metaDataAttributes.put("cards", null);

                metaDataMapByUUID.put(metaData.getId(), metaDataAttributes);
            }

            categorizedCardDataMap.put(category, metaDataMapByUUID);
        }

        return categorizedCardDataMap;
    }

    private void updateCategorizedCardData(Map<String, Map<UUID, Object>> categorizedCardData,
        Card card) {
        String category = card.getMetaData().getCategory();
        UUID metaDataId = card.getMetaData().getId();

        if (categorizedCardData.containsKey(category)) {
            Map<UUID, Object> dataById = categorizedCardData.get(category);

            if (dataById.containsKey(metaDataId)) {
                Map<String, Object> metaDataAttributes = (Map<String, Object>) dataById.get(
                    metaDataId);

                if (metaDataAttributes.get("cards") == null) {
                    List<Map<String, Object>> cardsList = new ArrayList<>();
                    Map<String, Object> cardAttributes = new HashMap<>();
                    cardAttributes.put("id", card.getId());
                    cardAttributes.put("seq", card.getSeq());
                    cardsList.add(cardAttributes);
                    metaDataAttributes.put("cards", cardsList);
                } else {
                    List<Map<String, Object>> cardsList = (List<Map<String, Object>>) metaDataAttributes.get(
                        "cards");
                    Map<String, Object> cardAttributes = new HashMap<>();
                    cardAttributes.put("id", card.getId());
                    cardAttributes.put("seq", card.getSeq());
                    cardsList.add(cardAttributes);
                }
            }
        }
    }

}
