package com.chanceToMe.MoonGuWanGu.repository;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
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
public class MetaDataRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MetaData insert(MetaData metaData) {
        String query = "insert into metadata (id, image_url, count, grade, weight, active, category) values (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(query, metaData.getId(), metaData.getImageUrl(), metaData.getCount(),
            metaData.getGrade(), metaData.getWeight(), metaData.getActive(),
            metaData.getCategory());

        return metaData;
    }

    public MetaData findById(UUID id) {
        String query = "select * from metadata where id = ?";

        return jdbcTemplate.queryForObject(query, rowMapper, id);
    }

    public MetaData findByIdWithLock(UUID id) {
        String query = "select * from metadata where id = ? for update";

        return jdbcTemplate.queryForObject(query, rowMapper, id);
    }

    public List<MetaData> findByCategory(String category) {
        String query = "select * from metadata where category = ?";

        return jdbcTemplate.query(query, rowMapper, category);
    }

    public List<MetaData> findActive() {
        String query = "select * from metadata where active = true order by weight desc";

        return jdbcTemplate.query(query, rowMapper);
    }

    public MetaData update(MetaData metaData) {
        String query = "update metadata set image_url = ?, count = ?, grade = ?, category = ? where id = ?";

        int affectedNum = jdbcTemplate.update(query, metaData.getImageUrl(), metaData.getCount(),
            metaData.getGrade(), metaData.getCategory(), metaData.getId());

        if (affectedNum == 1) {
            return metaData;
        } else {
            throw new CustomException(ErrorCode.NON_EXISTED, null);
        }
    }

    public UUID delete(UUID id) {
        String query = "delete from metadata where id = ?";

        int affectedNum = jdbcTemplate.update(query, id);

        if (affectedNum == 1) {
            return id;
        } else {
            throw new CustomException(ErrorCode.NON_EXISTED, null);
        }
    }

    private RowMapper<MetaData> rowMapper = new RowMapper<MetaData>() {
        @Override
        public MetaData mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MetaData(UUID.fromString(rs.getString("id")), rs.getString("image_url"),
                rs.getInt("count"), rs.getInt("grade"), rs.getInt("weight"),
                rs.getBoolean("active"), rs.getString("category"));
        }
    };
}
