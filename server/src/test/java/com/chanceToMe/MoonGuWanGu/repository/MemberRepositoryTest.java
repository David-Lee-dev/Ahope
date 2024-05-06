package com.chanceToMe.MoonGuWanGu.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.chanceToMe.MoonGuWanGu.model.Member;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberRepositoryTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    UUID nonExistedUUID = UUID.fromString("a0000000-1000-4000-8000-900000000000");

    @BeforeAll
    public void beforeAll() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/member.test.ddl.sql"));
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
        @DisplayName("Member 생성")
        void idealInsert() {
            Member member = Member.builder()
                    .id(UUID.randomUUID())
                    .email("test")
                    .build();

            memberRepository.insert(member);
        }

        @Test
        @DisplayName("email 중복 시 DuplicateKeyException 예외 발생")
        void duplicatedEmail() {
            Member member1 = Member.builder()
                    .id(UUID.randomUUID())
                    .email("test")
                    .build();
            Member member2 = Member.builder()
                    .id(UUID.randomUUID())
                    .email("test")
                    .build();


            memberRepository.insert(member1);

            assertThatThrownBy(() -> memberRepository.insert(member2)).isInstanceOf(DuplicateKeyException.class);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTest {

        UUID testMemberId;

        @BeforeEach
        void beforeEach() {
            testMemberId = insertTestMember();
        }

        @Test
        @DisplayName("id로 MetaData 조회")
        void ideal() {
            Member result = memberRepository.findById(testMemberId);

            assertThat(result.getId()).isEqualTo(testMemberId);
        }

        @Test
        @DisplayName("존재하지 않는 경우 EmptyResultDataAccessException 예외 발생")
        void nonExisted() {
            EmptyResultDataAccessException exception = catchThrowableOfType(
                () -> memberRepository.findById(nonExistedUUID),
                EmptyResultDataAccessException.class);

            assertThat(exception).isInstanceOf(EmptyResultDataAccessException.class);
        }
    }

    private UUID insertTestMember() {
        UUID testMetaDataId = UUID.randomUUID();
        String query = "insert into member (id, email, last_gacha_timestamp, remain_ticket) values (?, ?, ?, ?)";
        jdbcTemplate.update(query, testMetaDataId, "test@email.com", 0, 0);

        return testMetaDataId;
    }
}