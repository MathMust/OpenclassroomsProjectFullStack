package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.model.Topic;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {

    private String title;
    private String content;
    private Integer topicId;
}
