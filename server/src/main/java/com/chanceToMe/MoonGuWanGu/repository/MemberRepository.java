package com.chanceToMe.MoonGuWanGu.repository;

import com.chanceToMe.MoonGuWanGu.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class MemberRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Member insert(Member member) {
        String query = "insert into member (id, email, last_gacha_timestamp, remain_ticket) values (?, ?, ?, ?)";

        jdbcTemplate.update(
                query,
                member.getId(),
                member.getEmail(),
                member.getLastGachaTimestamp(),
                member.getRemainTicket()
        );

        return member;
    }
}
