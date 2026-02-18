package com.openclassrooms.mddapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class PostDto {

    private Integer id;
    private LocalDateTime date;
    private String title;
    private String content;
    private String authorName;
    private String topicTitle;
    private List<CommentDto> comments;
}