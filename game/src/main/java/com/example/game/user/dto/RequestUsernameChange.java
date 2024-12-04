package com.example.game.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUsernameChange {

    @NotEmpty
    @Pattern(regexp = "^[0-9a-zA-Z]{2,14}$", message = "닉네임은 특수문자를 제외한 2~14자리여야 합니다.")
    private String username;
}
