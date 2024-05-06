package com.chanceToMe.MoonGuWanGu.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.chanceToMe.MoonGuWanGu.model.Card;
import com.chanceToMe.MoonGuWanGu.model.Member;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import java.sql.Connection;
import java.util.List;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardRepositoryTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;


    UUID testMemberId = UUID.randomUUID();
    UUID testMetaDataId = UUID.randomUUID();
    Member testMember = Member.builder().id(testMemberId).email("test@test.com").remainTicket(3)
                              .lastGachaTimestamp(null).build();
    MetaData testMetaData = MetaData.builder().id(testMetaDataId).id(testMetaDataId)
                                    .imageUrl("test_image_url").count(0).grade(0)
                                    .category("test_category").build();

    @BeforeAll
    public void beforeAll() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/member.test.ddl.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/metadata.test.ddl.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/card.test.ddl.sql"));

            jdbcTemplate.update(
                "insert into member (id, email, last_gacha_timestamp, remain_ticket) values (?, ?, ?, ?)",
                testMember.getId(), testMember.getEmail(), testMember.getLastGachaTimestamp(),
                testMember.getRemainTicket());
            jdbcTemplate.update(
                "insert into metadata (id, image_url, count, grade, category) values (?, ?, ?, ?, ?)",
                testMetaData.getId(), testMetaData.getImageUrl(), testMetaData.getCount(),
                testMetaData.getGrade(), testMetaData.getCategory());
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
        @DisplayName("Card 생성")
        void ideal() {
            Card card = new Card(UUID.randomUUID(), testMember, testMetaData, 1L);

            cardRepository.insert(card);
        }
    }

    @Nested
    @DisplayName("findByMember")
    class FindByMemberTest {

        @BeforeEach
        void beforeEach() {
            String query = "insert into card (id, member, metadata, seq) values (?, ?, ?, ?)";

            jdbcTemplate.update(query, UUID.randomUUID(), testMemberId, testMetaDataId, 1L);
            jdbcTemplate.update(query, UUID.randomUUID(), testMemberId, testMetaDataId, 2L);
            jdbcTemplate.update(query, UUID.randomUUID(), testMemberId, testMetaDataId, 3L);
        }

        @Test
        @DisplayName("Card 리스트 조회")
        void ideal() {
            List<Card> result = cardRepository.findByMember(testMemberId);

            assertThat(result.size()).isEqualTo(3);
        }
    }

}