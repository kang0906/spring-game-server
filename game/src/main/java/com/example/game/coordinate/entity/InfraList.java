package com.example.game.coordinate.entity;

import lombok.Getter;

@Getter
public enum InfraList {

    TITANIUM_QUARRY("titaniumQuarry", 4000, 0),
    GAS_QUARRY("gasQuarry", 4000, 0),
//    FUEL_FACTORY("fuelFactory", 8000, 0),
    SHIP_YARD("shipYard", 15000, 5000);

    private InfraList(String name, int titaniumCost, int gasCost) {
        this.name = name;
        this.titaniumCost = titaniumCost;
        this.gasCost = gasCost;
    }

    public static InfraList findByName(String name){
        InfraList[] values = InfraList.values();
        for (InfraList value : values) {
            if(value.name.equals(name)){
                return value;
            }
        }
        return null;
    }

    private final String name;
    private final int titaniumCost;
    private final int gasCost;

}
