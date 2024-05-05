package com.chanceToMe.MoonGuWanGu.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import java.sql.Connection;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MetaDataRepositoryTest {

  @Autowired
  DataSource dataSource;

  @Autowired
  MetaDataRepository metaDataRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;

  UUID nonExistedUUID = UUID.fromString("a0000000-1000-4000-8000-900000000000");

  @BeforeAll
  public void beforeAll() throws Exception {
    try (Connection conn = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("/metaData.test.ddl.sql"));
    }
  }

  @AfterAll
  public void afterAll() throws Exception {
    try (Connection conn = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("/cleandb.test.ddl.sql"));
    }
  }

  @Nested
  @DisplayName("insert")
  class InsertTest {

    @Test
    @DisplayName("MetaData 생성")
    void ideal() {
      MetaData metaData = MetaData.builder().id(UUID.randomUUID()).imageUrl("test_image_url")
                                  .category("test_category").count(0).grade(0).build();

      MetaData result = metaDataRepository.insert(metaData);

      assertThat(result.getId()).isEqualTo(metaData.getId());
      assertThat(result.getImageUrl()).isEqualTo(metaData.getImageUrl());
      assertThat(result.getCategory()).isEqualTo(metaData.getCategory());
      assertThat(result.getCount()).isEqualTo(0);
      assertThat(result.getGrade()).isEqualTo(0);
    }
  }

  @Nested
  @DisplayName("findById")
  class FindByIdTest {

    UUID testMetaDataId;

    @BeforeEach
    void beforeEach() {
      testMetaDataId = insertTestMetaData();
    }

    @Test
    @DisplayName("id로 MetaData 조회")
    void ideal() {
      MetaData result = metaDataRepository.findById(testMetaDataId);

      assertThat(result.getId()).isEqualTo(testMetaDataId);
    }

    @Test
    @DisplayName("존재하지 않는 경우 EmptyResultDataAccessException 예외 발생")
    void nonExisted() {
      EmptyResultDataAccessException exception = catchThrowableOfType(
          () -> metaDataRepository.findById(nonExistedUUID), EmptyResultDataAccessException.class);

      assertThat(exception).isInstanceOf(EmptyResultDataAccessException.class);
    }
  }

  @Nested
  @DisplayName("update")
  class UpdateTest {

    UUID testMetaDataId;
    String updateImageUrl = "update_image_url";
    int updateCount = 5;
    int updateGrade = 5;
    String updateCategory = "update_category";

    @BeforeEach
    void beforeEach() {
      testMetaDataId = insertTestMetaData();
    }

    @Test
    @DisplayName("MetaData 수정")
    void ideal() {
      MetaData metaDataForUpdate = MetaData.builder().id(testMetaDataId).imageUrl(updateImageUrl)
                                           .category(updateCategory).count(updateCount)
                                           .grade(updateGrade).build();

      MetaData result = metaDataRepository.update(metaDataForUpdate);

      assertThat(result.getId()).isEqualTo(metaDataForUpdate.getId());
      assertThat(result.getImageUrl()).isEqualTo(metaDataForUpdate.getImageUrl());
      assertThat(result.getCategory()).isEqualTo(metaDataForUpdate.getCategory());
      assertThat(result.getCount()).isEqualTo(metaDataForUpdate.getCount());
      assertThat(result.getGrade()).isEqualTo(metaDataForUpdate.getGrade());
    }

    @Test
    @DisplayName("존재하지 않는 MetaData에 대해 CustomException 발생")
    void nonExisted() {
      MetaData metaDataForUpdate = MetaData.builder().id(nonExistedUUID).imageUrl(updateImageUrl)
                                           .category(updateCategory).count(updateCount)
                                           .grade(updateGrade).build();

      assertThatThrownBy(() -> metaDataRepository.update(metaDataForUpdate)).isInstanceOf(
          CustomException.class);
    }
  }

  @Nested
  @DisplayName("delete")
  class DeleteTest {

    UUID testMetaDataId;

    @BeforeEach
    void beforeEach() {
      testMetaDataId = insertTestMetaData();
    }

    @Test
    @DisplayName("MetaData 삭제")
    void ideal() {
      UUID result = metaDataRepository.delete(testMetaDataId);

      assertThat(result).isEqualTo(testMetaDataId);
    }

    @Test
    @DisplayName("존재하지 않는 MetaData에 대해 CustomException 발생")
    void nonExisted() {
      assertThatThrownBy(() -> metaDataRepository.delete(nonExistedUUID)).isInstanceOf(
          CustomException.class);
    }
  }

  private UUID insertTestMetaData() {
    UUID testMetaDataId = UUID.randomUUID();
    String query = "insert into metadata (id, image_url, count, grade, category) values (?, ?, ?, ?, ?)";
    jdbcTemplate.update(query, testMetaDataId, "test_image_url", 0, 0, "test_category");

    return testMetaDataId;
  }

  private UUID insertTestMetaData(String imageUrl, int count, int grade, String category) {
    UUID testMetaDataId = UUID.randomUUID();
    String query = "insert into metadata (id, image_url, count, grade, category) values (?, ?, ?, ?, ?)";
    jdbcTemplate.update(query, testMetaDataId, imageUrl, count, grade, category);

    return testMetaDataId;
  }
}