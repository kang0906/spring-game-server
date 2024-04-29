package com.example.game.unit.entity;

import lombok.Getter;

@Getter
public enum UnitType {

    INFANTRY("infantry", 100, 10, 0, 1, 500),
    ARTILLERY("artillery", 40, 15, 0, 3, 200),
    CAVALRY("cavalry", 200, 20, 0, 1, 300)
    ;

    private String name;
    private int maxHp;
    private int ap;
    private int dp;
    private int range;
    private int itemCapacity;

    UnitType(String name, int maxHp, int ap, int dp, int range, int itemCapacity) {
        this.name = name;
        this.maxHp = maxHp;
        this.ap = ap;
        this.dp = dp;
        this.range = range;
        this.itemCapacity = itemCapacity;
    }
}
