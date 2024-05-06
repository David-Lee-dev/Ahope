package com.chanceToMe.MoonGuWanGu.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class Card {

  @Id
  private UUID id;

  private Member member;

  private MetaData metaData;

  private Long seq;

  @Builder
  public Card(UUID id, Member member, MetaData metaData, Long seq) {
    this.id = id;
    this.member = member;
    this.metaData = metaData;
    this.seq = seq;
  }
}
