package com.chanceToMe.MoonGuWanGu.service;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import com.chanceToMe.MoonGuWanGu.repository.MetaDataRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class MetaDataService {

  @Autowired
  MetaDataRepository metaDataRepository;

  public MetaData addMetaData(String imageUrl, int grade, int weight, String category) {
    try {
      MetaData metaData = new MetaData(UUID.randomUUID(), imageUrl, 0, grade, weight, false,
          category);

      return metaDataRepository.insert(metaData);
    } catch (Exception e) {
      if (e instanceof DuplicateKeyException) {
        throw new CustomException(ErrorCode.DUPLICATED_KEY, e.getStackTrace());
      } else {
        throw new CustomException(ErrorCode.UNKNOWN, e.getStackTrace());
      }
    }
  }

  public List<MetaData> retrieveMetaDataByCategory(String category) {
    List<MetaData> metaDataList = metaDataRepository.findByCategory(category);

    if (metaDataList.isEmpty()) {
      throw new CustomException(ErrorCode.NON_EXISTED, null);
    }

    return metaDataList;
  }

  public MetaData updateMetaData(UUID id, String imageUrl, Integer count, Integer grade,
      String category) {
    try {
      MetaData metaData = metaDataRepository.findById(id);
      MetaData metaDataToUpdate = MetaData.builder().id(id).imageUrl(
                                              imageUrl == null ? metaData.getImageUrl() : imageUrl)
                                          .count(count == null ? metaData.getCount() : count)
                                          .grade(grade == null ? metaData.getGrade() : grade)
                                          .category(
                                              category == null ? metaData.getCategory() : category)
                                          .build();

      return metaDataRepository.update(metaDataToUpdate);

    } catch (Exception e) {
      System.out.println(e);
      if (e instanceof CustomException) {
        throw e;
      } else if (e instanceof EmptyResultDataAccessException) {
        throw new CustomException(ErrorCode.NON_EXISTED, e.getStackTrace());
      } else {
        throw new CustomException(ErrorCode.UNKNOWN, e.getStackTrace());
      }
    }
  }

  public UUID deleteMetaData(UUID id) {
    try {
      return metaDataRepository.delete(id);
    } catch (Exception e) {
      if (e instanceof CustomException) {
        throw e;
      } else {
        throw new CustomException(ErrorCode.UNKNOWN, e.getStackTrace());
      }
    }
  }
}
