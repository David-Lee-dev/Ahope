package com.chanceToMe.MoonGuWanGu.repository;

import com.chanceToMe.MoonGuWanGu.model.Member;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberRepositoryTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    MemberRepository memberRepository;

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
}