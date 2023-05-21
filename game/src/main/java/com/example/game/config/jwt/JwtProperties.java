package com.example.game.config.jwt;

public interface JwtProperties {
    String CLAIM_USER_EMAIL = "USER_EMAIL";
    String SUBJECT = "JwtToken";
    int EXPIRATION_TIME = 864000000;    // 10일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
