package com.example.game.user.dto;

import com.example.game.user.entity.User;
import lombok.Getter;

@Getter
public class MyInfoResponseDto {
    private String email;
    private String kakaoId;
    private String username;

    public MyInfoResponseDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.kakaoId = user.getKakaoId();
    }
}
