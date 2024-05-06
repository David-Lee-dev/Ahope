package com.chanceToMe.MoonGuWanGu.repository;

import com.chanceToMe.MoonGuWanGu.model.Card;
import com.chanceToMe.MoonGuWanGu.model.Member;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CardRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Card insert(Card card) {
        String query = "insert into card (id, member, metadata, seq) values (?, ?, ?, ?)";

        jdbcTemplate.update(query, card.getId(), card.getMember().getId(),
            card.getMetaData().getId(), card.getSeq());

        return card;
    }

    public List<Card> findByMember(UUID memberId) {
        String query = """
            SELECT c.id AS id, c.seq AS seq, m.id AS member, md.id AS metadata, md.image_url AS image_url, md.grade AS grade, md.category AS category 
            FROM card c 
            JOIN member m ON c.member = m.id 
            JOIN metadata md ON c.metadata = md.id 
            WHERE c.member = ?;
            """;

        return jdbcTemplate.query(query, rowMapper, memberId);
    }

    private RowMapper<Card> rowMapper = new RowMapper<Card>() {
        @Override
        public Card mapRow(ResultSet rs, int rowNum) throws SQLException {
            System.out.println(rs);
            UUID memberId = UUID.fromString(rs.getString("member"));
            Member member = Member.builder().id(memberId).build();

            UUID metaDataId = UUID.fromString(rs.getString("metadata"));
            String imageUrl = rs.getString("image_url");
            Integer grade = rs.getInt("grade");
            String category = rs.getString("category");
            MetaData metaData = MetaData.builder().id(metaDataId).imageUrl(imageUrl).grade(grade)
                                        .category(category).build();

            UUID cardId = UUID.fromString(rs.getString("id"));
            Long seq = rs.getLong("seq");

            return new Card(cardId, member, metaData, seq);
        }
    };
}
