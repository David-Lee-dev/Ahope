package com.chanceToMe.MoonGuWanGu.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
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
import org.springframework.dao.DuplicateKeyException;
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
            String testImageUrl = "test";
            int testCount = 123;
            int testGrade = 123;
            int testWeight = 123;
            String testCategory = "test";

            MetaData metaData = new MetaData(UUID.randomUUID(), testImageUrl, testCount, testGrade,
                testWeight, false, testCategory);

            MetaData result = metaDataRepository.insert(metaData);

            assertThat(result.getId()).isEqualTo(metaData.getId());
            assertThat(result.getImageUrl()).isEqualTo(testImageUrl);
            assertThat(result.getCount()).isEqualTo(testCount);
            assertThat(result.getGrade()).isEqualTo(testGrade);
            assertThat(result.getWeight()).isEqualTo(testWeight);
            assertThat(result.getCategory()).isEqualTo(testCategory);
        }

        @Test
        @DisplayName("imageUrl 중복 시 DuplicateKeyException 발생")
        void duplicatedImageUrl() {
            String testImageUrl = "test";
            int testCount = 123;
            int testGrade = 123;
            int testWeight = 123;
            String testCategory = "test";
            MetaData metaData = new MetaData(UUID.randomUUID(), testImageUrl, testCount, testGrade,
                testWeight, false, testCategory);

            insertTestMetaData(testImageUrl, testCount, testGrade, testWeight, false, testCategory);

            assertThatThrownBy(() -> metaDataRepository.insert(metaData)).isInstanceOf(
                DuplicateKeyException.class);
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
                () -> metaDataRepository.findById(nonExistedUUID),
                EmptyResultDataAccessException.class);

            assertThat(exception).isInstanceOf(EmptyResultDataAccessException.class);
        }
    }

    @Nested
    @DisplayName("findByIdWithLock")
    class FindByIdWithLockTest {

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
                () -> metaDataRepository.findById(nonExistedUUID),
                EmptyResultDataAccessException.class);

            assertThat(exception).isInstanceOf(EmptyResultDataAccessException.class);
        }
    }

    @Nested
    @DisplayName("findByCategory")
    class FindByCategoryTest {

        String testCategory = "category";

        @BeforeEach
        void beforeEach() {
            insertTestMetaData("image_url1", 0, 0, 0, false, testCategory);
            insertTestMetaData("image_url2", 0, 0, 0, false, testCategory);
            insertTestMetaData("image_url3", 0, 0, 0, false, testCategory);
        }

        @Test
        @DisplayName("category로 MetaData 조회")
        void ideal() {
            List<MetaData> result = metaDataRepository.findByCategory(testCategory);

            assertThat(result.size()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("findByCategory")
    class FindActiveTest {

        String testCategory = "category";

        @BeforeEach
        void beforeEach() {
            insertTestMetaData("image_url1", 0, 0, 0, false, testCategory);
            insertTestMetaData("image_url2", 0, 0, 0, false, testCategory);
            insertTestMetaData("image_url3", 0, 0, 0, true, testCategory);
        }

        @Test
        @DisplayName("active값이 true인 MetaData 조회")
        void ideal() {
            List<MetaData> result = metaDataRepository.findActive();

            assertThat(result.size()).isEqualTo(1);
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
            MetaData metaDataForUpdate = MetaData.builder().id(testMetaDataId)
                                                 .imageUrl(updateImageUrl).category(updateCategory)
                                                 .count(updateCount).grade(updateGrade).build();

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
            MetaData metaDataForUpdate = MetaData.builder().id(nonExistedUUID)
                                                 .imageUrl(updateImageUrl).category(updateCategory)
                                                 .count(updateCount).grade(updateGrade).build();

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

    @SuppressWarnings("unchecked")
    @Nested
    @DisplayName("getMetadataListByCategory")
    class GetMetadataListByCategoryTest {

        @BeforeEach
        void beforeEach() {
            insertTestMetaData("test1", 0, 0, 0, false, "category1");
            insertTestMetaData("test2", 0, 0, 0, false, "category1");
            insertTestMetaData("test3", 0, 0, 0, false, "category2");
        }

        @Test
        @DisplayName("category 별 MetaData 개수 조회")
        void ideal() {
            List<Map<String, Object>> result = metaDataRepository.getMetadataListByCategory();

            assertThat(result.size()).isEqualTo(2);
            for(Map<String, Object> item : result) {
                if (item.get("category") == "category1") {
                    assertThat(((List<UUID>) item.get("idList")).size()).isEqualTo(2);
                } else if (item.get("category") == "category2"){
                    assertThat(((List<UUID>) item.get("idList")).size()).isEqualTo(1);
                }
            }
        }
    }

    private UUID insertTestMetaData() {
        UUID testMetaDataId = UUID.randomUUID();
        String query = "insert into metadata (id, image_url, count, grade, weight, active, category) values (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, testMetaDataId, "test", 0, 0, 0, false, "test");

        return testMetaDataId;
    }

    private UUID insertTestMetaData(String imageUrl, Integer count, Integer grade, Integer weight,
        Boolean active, String category) {
        UUID testMetaDataId = UUID.randomUUID();
        String query = "insert into metadata (id, image_url, count, grade, weight, active, category) values (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, testMetaDataId, imageUrl, count, grade, weight, active,
            category);

        return testMetaDataId;
    }
}