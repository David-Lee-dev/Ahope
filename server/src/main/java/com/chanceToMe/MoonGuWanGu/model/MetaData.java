package com.chanceToMe.MoonGuWanGu.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class MetaData {

    @Id
    int id;

    String image_url;

    int count;

    Short grade;

    String category;
}
