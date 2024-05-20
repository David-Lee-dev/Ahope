package com.chanceToMe.MoonGuWanGu.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor
public class MetaData {

    @Id
    UUID id;

    String imageUrl;

    Integer count;

    Integer grade;

    Integer weight;

    Boolean active;

    String category;

    @Builder
    public MetaData(UUID id, String imageUrl, Integer count, Integer grade, Integer weight,
        Boolean active, String category) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.count = count;
        this.grade = grade;
        this.weight = weight;
        this.active = active;
        this.category = category;
    }

    public void increaseCount() {
        this.count++;
    }
}
