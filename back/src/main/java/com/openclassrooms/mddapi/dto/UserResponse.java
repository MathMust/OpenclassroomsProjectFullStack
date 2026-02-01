package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserResponse {

    private Integer id;
    private String email;
    private String name;
    private List<TopicDto> topics;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.topics = new ArrayList<>();
    }
}
