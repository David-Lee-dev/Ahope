package com.chanceToMe.MoonGuWanGu.repository;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.Member;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MemberRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Member insert(Member member) {
        String query = "insert into member (id, email, last_gacha_timestamp, remain_ticket) values (?, ?, ?, ?)";

        jdbcTemplate.update(query, member.getId(), member.getEmail(),
            member.getLastGachaTimestamp(), member.getRemainTicket());

        return member;
    }

    public Member findById(UUID id) {
        String query = "select * from member where id = ?";

        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
            return new Member(UUID.fromString(rs.getString("id")), rs.getString("email"),
                rs.getLong("last_gacha_timestamp"), rs.getInt("remain_ticket"));
        }, id);
    }

    public Member findByEmail(String email) {
        String query = "select * from member where email = ?";

        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
            return new Member(UUID.fromString(rs.getString("id")), rs.getString("email"),
                rs.getLong("last_gacha_timestamp"), rs.getInt("remain_ticket"));
        }, email);
    }

    public Member update(Member member) {
        String query = "update member set last_gacha_timestamp = ?, remain_ticket = ? where id = ?";

        int affectedNum = jdbcTemplate.update(query, member.getLastGachaTimestamp(),
            member.getRemainTicket(), member.getId());

        if (affectedNum == 1) {
            return member;
        } else {
            throw new CustomException(ErrorCode.NON_EXISTED, null);
        }
    }
}
