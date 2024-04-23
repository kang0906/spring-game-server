package com.example.game.unit.entity;

import lombok.Getter;

@Getter
public enum UnitType {

    INFANTRY("보병", 100, 10, 0),
    ARTILLERY("포병", 40, 25, 0),
    CAVALRY("기병", 200, 20, 0)
    ;

    private String name;
    private int maxHp;
    private int ap;
    private int dp;

    UnitType(String name, int maxHp, int ap, int dp) {
        this.name = name;
        this.maxHp = maxHp;
        this.ap = ap;
        this.dp = dp;
    }
}
