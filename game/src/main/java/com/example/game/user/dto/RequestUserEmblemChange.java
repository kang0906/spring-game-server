package com.example.game.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserEmblemChange {
    @NotEmpty
    @Length(max=30, message = "잘못된 엠블럼 형식입니다.")
    private String emblem;
}
