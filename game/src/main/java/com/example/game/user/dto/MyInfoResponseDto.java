package com.example.game.user.dto;

import com.example.game.user.entity.User;
import lombok.Getter;

@Getter
public class MyInfoResponseDto {
    private String username;
    private String emblem;
    private int killCount;
    private int killPoint;

    public MyInfoResponseDto(User user) {
        this.username = user.getUsername();
        this.emblem = user.getEmblem();
        this.killCount = user.getKillCount();
        this.killPoint = user.getKillPoint();
    }
}
