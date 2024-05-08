package com.chanceToMe.MoonGuWanGu.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.chanceToMe.MoonGuWanGu.dto.CreateMetaDataDto;
import com.chanceToMe.MoonGuWanGu.dto.DeleteMetaDataDto;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import com.chanceToMe.MoonGuWanGu.service.MetaDataService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MetaDataControllerTest {

  @InjectMocks
  MetaDataController metaDataController;

  @Mock
  MetaDataService metaDataService;

  @Nested
  @DisplayName("add")
  class AddTest {

    @Test
    @DisplayName("MetaData 생성 성공 시 201 응답")
    void ideal() {
      when(metaDataService.addMetaData(anyString(), anyInt(), anyInt(),anyString())).thenReturn(
          MetaData.builder().build());

      ResponseEntity result = metaDataController.add(new CreateMetaDataDto("test", 0, 0, "test"));
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
  }

  @Nested
  @DisplayName("find")
  class FindTest {

    @Test
    @DisplayName("MetaData 조회 성공 시 list와 함께 200 응답")
    void ideal() {
      List<MetaData> metaDataList = new ArrayList<>();
      metaDataList.add(MetaData.builder().build());
      metaDataList.add(MetaData.builder().build());
      metaDataList.add(MetaData.builder().build());

      Map<String, Object> expectedResponseBody = new HashMap<>();
      expectedResponseBody.put("list", metaDataList);

      when(metaDataService.retrieveMetaDataByCategory(anyString())).thenReturn(metaDataList);

      ResponseEntity result = metaDataController.find("category");

      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(result.getBody()).isEqualTo(expectedResponseBody);
    }
  }

  @Nested
  @DisplayName("delete")
  class DeleteTest {

    @Test
    @DisplayName("MetaData 삭제 성공 시 200 응답")
    void ideal() {
      UUID testId = UUID.randomUUID();
      when(metaDataService.deleteMetaData(testId)).thenReturn(testId);

      ResponseEntity result = metaDataController.delete(new DeleteMetaDataDto(testId));
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }
}