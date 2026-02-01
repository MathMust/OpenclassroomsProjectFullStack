package com.openclassrooms.mddapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentsResponse {
    private List<CommentDto> comments;
}
