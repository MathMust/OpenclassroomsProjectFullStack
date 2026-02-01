package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PostDto {

    private Integer id;
    private LocalDateTime date;
    private String title;
    private String content;
    private String authorName;
}