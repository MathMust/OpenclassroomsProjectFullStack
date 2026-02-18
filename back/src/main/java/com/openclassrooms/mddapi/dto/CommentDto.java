package com.openclassrooms.mddapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentDto {

    private Integer id;
    private LocalDateTime date;
    private String content;
    private String userName;
}