package com.openclassrooms.mddapi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopicDto {

    private Integer id;
    private String title;
    private String description;
    private boolean subscription;
}