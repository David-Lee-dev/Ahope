package com.chanceToMe.MoonGuWanGu.repository;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MetaDataRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public MetaData insert(MetaData metaData) {
    String query = "insert into metadata (id, image_url, count, grade, category) values (?, ?, ?, ?, ?)";

    jdbcTemplate.update(query, metaData.getId(), metaData.getImageUrl(), metaData.getCount(),
        metaData.getGrade(), metaData.getCategory());

    return metaData;
  }

  public MetaData findById(UUID id) {
    String query = "select * from metadata where id = ?";

    return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
      return MetaData.builder()
                     .id(UUID.fromString(rs.getString("id")))
                     .imageUrl(rs.getString("image_url"))
                     .category(rs.getString("category"))
                     .count(rs.getInt("count"))
                     .grade(rs.getInt("grade"))
                     .build();

    }, id);
  }

  public List<MetaData> findByCategory(String category) {
    String query = "select * from metadata where category = ?";

    return jdbcTemplate.query(query, (rs, rowNum) -> {
      return MetaData.builder()
                     .id(UUID.fromString(rs.getString("id")))
                     .imageUrl(rs.getString("image_url"))
                     .category(rs.getString("category"))
                     .count(rs.getInt("count"))
                     .grade(rs.getInt("grade"))
                     .build();

    }, category);
  }

  public MetaData update(MetaData metaData) {
    String query = "update metadata set image_url = ?, count = ?, grade = ?, category = ? where id = ?";

    int affectedNum = jdbcTemplate.update(query, metaData.getImageUrl(), metaData.getCount(),
        metaData.getGrade(),
        metaData.getCategory(), metaData.getId());

    if (affectedNum == 1) {
      return metaData;
    } else {
      throw new CustomException(ErrorCode.NON_EXISTED);
    }
  }

  public UUID delete(UUID id) {
    String query = "delete from metadata where id = ?";

    int affectedNum = jdbcTemplate.update(query, id);

    if (affectedNum == 1) {
      return id;
    } else {
      throw new CustomException(ErrorCode.NON_EXISTED);
    }
  }
}
