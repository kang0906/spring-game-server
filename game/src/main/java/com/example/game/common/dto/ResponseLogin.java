package com.example.game.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseLogin {

    private Boolean success;
    private String message;
    private String token;
    private long x;
    private long y;


    @Builder
    public ResponseLogin(Boolean success, String message, String token, long x, long y) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.x = x;
        this.y = y;
    }


}
