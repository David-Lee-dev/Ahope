package com.chanceToMe.MoonGuWanGu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import com.chanceToMe.MoonGuWanGu.repository.MetaDataRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

@ExtendWith(MockitoExtension.class)
class MetaDataServiceTest {

  @InjectMocks
  MetaDataService metaDataService;

  @Mock
  MetaDataRepository metaDataRepository;

  @Nested
  @DisplayName("addMetaData")
  class AddMetaDataTest {

    String testImageUrl = "test_image_url";
    String testCategory = "test_category";

    @Test
    @DisplayName("중복된 imageUrl이 아니면 MetaData 추가 후 추가된 데이터 반환")
    void ideal() {
      MetaData metaData = MetaData.builder().id(UUID.randomUUID()).imageUrl(testImageUrl).count(0)
                                  .grade(0).category(testCategory).build();

      when(metaDataRepository.insert(any(MetaData.class))).thenReturn(metaData);

      MetaData result = metaDataService.addMetaData(testImageUrl, 0, testCategory);

      assertThat(result.getImageUrl()).isEqualTo(testImageUrl);
      assertThat(result.getCount()).isEqualTo(0);
      assertThat(result.getGrade()).isEqualTo(0);
      assertThat(result.getCategory()).isEqualTo(testCategory);
    }


    @Test
    @DisplayName("0 미만의 grade는 0으로 설정하여 추가 후 추가된 데이터 반환")
    void gradeUnderZero() {
      MetaData metaData = MetaData.builder().id(UUID.randomUUID()).imageUrl(testImageUrl).count(0)
                                  .grade(0).category(testCategory).build();

      when(metaDataRepository.insert(any(MetaData.class))).thenReturn(metaData);

      MetaData result = metaDataService.addMetaData(testImageUrl, -2, testCategory);

      assertThat(result.getImageUrl()).isEqualTo(testImageUrl);
      assertThat(result.getCount()).isEqualTo(0);
      assertThat(result.getGrade()).isEqualTo(0);
      assertThat(result.getCategory()).isEqualTo(testCategory);
    }

    @Test
    @DisplayName("중복된 imageUrl일 경우 DUPLICATED_KEY 예외 발생")
    void duplicatedEmail() {
      when(metaDataRepository.insert(any(MetaData.class))).thenThrow(DuplicateKeyException.class);

      CustomException exception = catchThrowableOfType(
          () -> metaDataService.addMetaData(testImageUrl, 0, testCategory), CustomException.class);

      assertThat(exception).isInstanceOf(CustomException.class);
      assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_KEY);
    }
  }

  @Nested
  @DisplayName("updateMetaData")
  class UpdateMetaDataTest {

    UUID testUUID = UUID.randomUUID();

    MetaData metaDataOriginal = MetaData.builder().id(testUUID).imageUrl("original").count(0)
                                        .grade(1).category("original").build();
    MetaData metaDataUpdated = MetaData.builder().id(testUUID).imageUrl("updated").count(100)
                                       .grade(5).category("updated").build();

    @Test
    @DisplayName("MetaData 변경 후 변경된 데이터 반환")
    void ideal() {
      when(metaDataRepository.update(any(MetaData.class))).thenReturn(metaDataUpdated);

      MetaData result = metaDataService.updateMetaData(metaDataUpdated.getId(),
          metaDataUpdated.getImageUrl(), metaDataUpdated.getCount(), metaDataUpdated.getGrade(),
          metaDataUpdated.getCategory());

      assertThat(result.getImageUrl()).isEqualTo(metaDataUpdated.getImageUrl());
      assertThat(result.getCount()).isEqualTo(metaDataUpdated.getCount());
      assertThat(result.getGrade()).isEqualTo(metaDataUpdated.getGrade());
      assertThat(result.getCategory()).isEqualTo(metaDataUpdated.getCategory());
    }

    @Test
    @DisplayName("null로 입력된 property들은 변경되지 않음")
    void nullInput() {
      MetaData metaDataUpdated = MetaData.builder().id(testUUID).imageUrl("original").count(0)
                                         .grade(1).category("original").build();
      when(metaDataRepository.findById(any(UUID.class))).thenReturn(metaDataOriginal);
      when(metaDataRepository.update(any(MetaData.class))).thenReturn(metaDataUpdated);

      MetaData result = metaDataService.updateMetaData(metaDataUpdated.getId(), null,
          metaDataUpdated.getCount(), null, null);

      assertThat(result.getImageUrl()).isEqualTo(metaDataOriginal.getImageUrl());
      assertThat(result.getCount()).isEqualTo(metaDataUpdated.getCount());
      assertThat(result.getGrade()).isEqualTo(metaDataOriginal.getGrade());
      assertThat(result.getCategory()).isEqualTo(metaDataOriginal.getCategory());
    }

    @Test
    @DisplayName("존재하지 않는 MetaData일 경우 NOT_EXISTED 예외 발생")
    void nonExisted() {
      when(metaDataRepository.findById(any(UUID.class))).thenThrow(
          EmptyResultDataAccessException.class);

      CustomException exception = catchThrowableOfType(
          () -> metaDataService.updateMetaData(metaDataUpdated.getId(),
              metaDataUpdated.getImageUrl(), metaDataUpdated.getCount(), metaDataUpdated.getGrade(),
              metaDataUpdated.getCategory()), CustomException.class);

      assertThat(exception).isInstanceOf(CustomException.class);
      assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NON_EXISTED);
    }
  }
}