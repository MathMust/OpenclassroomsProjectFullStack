package com.openclassrooms.mddapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthSuccess {
    private String token;

    public AuthSuccess(String token) {
        this.token = token;
    }
}
