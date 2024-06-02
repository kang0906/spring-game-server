package com.example.game.facility.entity;

import lombok.Getter;

@Getter
public enum FacilityType {

    HEADQUARTERS("headquarters", 1000, 10, 0, 1, 0),
    INFANTRY_SCHOOL("infantry school", 400, 0, 0, 0, 100),
    ARTILLERY_SCHOOL("artillery school", 400, 0, 0, 0, 150),
    CAVALRY_SCHOOL("cavalry school", 400, 0, 0, 0, 200),
    STEEL_FACTORY("steel factory", 350, 0, 0, 0, 120),
    FARM("farm", 350, 0, 0, 0, 120)
    ;

    private String name;
    private int maxHp;
    private int ap;
    private int dp;
    private int range;
    private int steelCostToCreate;

    FacilityType(String name, int maxHp, int ap, int dp, int range, int facilityCreateSteelCost) {
        this.name = name;
        this.maxHp = maxHp;
        this.ap = ap;
        this.dp = dp;
        this.range = range;
        this.steelCostToCreate = facilityCreateSteelCost;
    }
}
