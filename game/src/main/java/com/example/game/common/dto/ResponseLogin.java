package com.example.game.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseLogin {

    private Boolean success;
    private String message;
    private String token;

    @Builder
    public ResponseLogin(Boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }


}
