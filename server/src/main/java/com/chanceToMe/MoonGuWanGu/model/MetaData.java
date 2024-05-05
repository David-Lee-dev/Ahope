package com.chanceToMe.MoonGuWanGu.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class MetaData {

  @Id
  UUID id;

  String imageUrl;

  int count;

  int grade;

  String category;

  @Builder
  public MetaData(UUID id, String imageUrl, int count, int grade, String category) {
    this.id = id;
    this.imageUrl = imageUrl;
    this.count = count;
    this.grade = grade;
    this.category = category;
  }
}
