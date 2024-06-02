package com.example.game.unit.entity;

import lombok.Getter;

@Getter
public enum UnitType {

    INFANTRY("infantry", 100, 10, 0, 1, 500, 100, 100),
    ARTILLERY("artillery", 40, 15, 0, 3, 200, 150, 100),
    CAVALRY("cavalry", 200, 20, 0, 1, 300, 200, 100)
    ;

    private String name;
    private int maxHp;
    private int ap;
    private int dp;
    private int range;
    private int itemCapacity;
    private int steelCostToCreate;
    private int foodCostToCreate;

    UnitType(String name, int maxHp, int ap, int dp, int range, int itemCapacity, int steelCostToCreate, int foodCostToCreate) {
        this.name = name;
        this.maxHp = maxHp;
        this.ap = ap;
        this.dp = dp;
        this.range = range;
        this.itemCapacity = itemCapacity;
        this.steelCostToCreate = steelCostToCreate;
        this.foodCostToCreate = foodCostToCreate;
    }
}
