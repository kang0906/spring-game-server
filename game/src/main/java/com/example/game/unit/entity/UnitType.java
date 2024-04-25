package com.example.game.unit.entity;

import lombok.Getter;

@Getter
public enum UnitType {

    INFANTRY("보병", 100, 10, 0, 1),
    ARTILLERY("포병", 40, 15, 0, 3),
    CAVALRY("기병", 200, 20, 0, 1)
    ;

    private String name;
    private int maxHp;
    private int ap;
    private int dp;
    private int range;

    UnitType(String name, int maxHp, int ap, int dp, int range) {
        this.name = name;
        this.maxHp = maxHp;
        this.ap = ap;
        this.dp = dp;
        this.range = range;
    }
}
