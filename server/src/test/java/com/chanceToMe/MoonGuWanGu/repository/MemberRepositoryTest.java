package com.chanceToMe.MoonGuWanGu.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.Member;
import java.sql.Connection;
import java.time.Instant;
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
    String existedEmail = "existed@test.com";
    String nonExistedEmail = "nonExisted@test.com";

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

        @BeforeEach
        void beforeEach() {
            insertTestMember(existedEmail);
        }

        @Test
        @DisplayName("Member 생성")
        void idealInsert() {
            Member member = Member.builder().id(UUID.randomUUID()).email(nonExistedEmail).build();

            memberRepository.insert(member);
        }

        @Test
        @DisplayName("email 중복 시 DuplicateKeyException 예외 발생")
        void duplicatedEmail() {
            Member member = Member.builder().id(UUID.randomUUID()).email(existedEmail).build();

            assertThatThrownBy(() -> memberRepository.insert(member)).isInstanceOf(
                DuplicateKeyException.class);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTest {

        UUID testMemberId;

        @BeforeEach
        void beforeEach() {
            testMemberId = insertTestMember(existedEmail);
        }

        @Test
        @DisplayName("id로 Member 조회")
        void ideal() {
            Member result = memberRepository.findById(testMemberId);

            assertThat(result.getId()).isEqualTo(testMemberId);
        }

        @Test
        @DisplayName("존재하지 않는 경우 EmptyResultDataAccessException 예외 발생")
        void nonExisted() {
            assertThatThrownBy(() -> memberRepository.findById(nonExistedUUID)).isInstanceOf(
                EmptyResultDataAccessException.class);
        }
    }

    @Nested
    @DisplayName("findByEmail")
    class FindByEmailTest {

        String testMemberEmail = "test@test.com";

        @BeforeEach
        void beforeEach() {
            insertTestMember(testMemberEmail);
        }

        @Test
        @DisplayName("email로 Member 조회")
        void ideal() {
            Member result = memberRepository.findByEmail(testMemberEmail);

            assertThat(result.getEmail()).isEqualTo(testMemberEmail);
        }

        @Test
        @DisplayName("존재하지 않는 경우 EmptyResultDataAccessException 예외 발생")
        void nonExisted() {
            assertThatThrownBy(() -> memberRepository.findById(nonExistedUUID)).isInstanceOf(
                EmptyResultDataAccessException.class);
        }
    }

    @Nested
    @DisplayName("update")
    class UpdateTest {

        UUID testMemberId;

        @BeforeEach
        void beforeEach() {
            testMemberId = insertTestMember(existedEmail);
        }

        @Test
        @DisplayName("Member 수정")
        void ideal() {
            Long timestamp = Instant.now().toEpochMilli();
            Member member = new Member(testMemberId, "update@test.com", timestamp, 1);

            Member result = memberRepository.update(member);

            assertThat(result.getId()).isEqualTo(member.getId());
            assertThat(result.getEmail()).isEqualTo(member.getEmail());
            assertThat(result.getLastGachaTimestamp()).isEqualTo(member.getLastGachaTimestamp());
            assertThat(result.getRemainTicket()).isEqualTo(member.getRemainTicket());
        }

        @Test
        @DisplayName("존재하지 않는 Member일 경우 NON_EXISTED 예외 발생")
        void duplicatedEmail() {
            Long timestamp = Instant.now().toEpochMilli();
            Member member = new Member(nonExistedUUID, existedEmail, timestamp, 1);

            CustomException exception = catchThrowableOfType(() -> memberRepository.update(member),
                CustomException.class);

            assertThat(exception).isInstanceOf(CustomException.class);
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NON_EXISTED);
        }
    }

    private UUID insertTestMember(String email) {
        UUID testMetaDataId = UUID.randomUUID();
        String query = "insert into member (id, email, last_gacha_timestamp, remain_ticket) values (?, ?, ?, ?)";
        jdbcTemplate.update(query, testMetaDataId, email, 0, 0);

        return testMetaDataId;
    }
}