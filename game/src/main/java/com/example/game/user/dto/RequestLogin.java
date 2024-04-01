package com.example.game.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RequestLogin {
    @NotNull(message = "이메일은 필수 입력 값입니다.")
    private String email;
    private String password;
    private String username;
}
