package com.example.game.system.value.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameSystemValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameSystemValueId;

    private String property;
    private String gameSystemValue;
    private String description;

    public GameSystemValue(String property, String gameSystemValue, String description) {
        this.property = property;
        this.gameSystemValue = gameSystemValue;
        this.description = description;
    }

    public void changeGameSystemValue(String newGameSystemValue) {
        this.gameSystemValue = newGameSystemValue;
    }
}
