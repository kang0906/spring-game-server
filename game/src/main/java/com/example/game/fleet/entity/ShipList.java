package com.example.game.fleet.entity;


import lombok.Getter;

@Getter
public enum ShipList {

    SCOUT("Scout", 1, 10,10,100,0,1),
    FRIGATE("Frigate", 10, 100,100,500,150,5),
    DESTROYER("Destroyer", 100, 1000,1000,8000,2000,80),
    CRUISER("Cruiser", 1000, 10000,10000,70000,18000,700),
    BATTLESHIP("Battleship", 10000, 100000,100000,650000,160000,3000);


    ShipList(String name, int attack, int maxHp, int capacity, int titaniumCost, int gasCost,int productionTime) {
        this.name = name;
        this.attack = attack;
        this.maxHp = maxHp;
        this.capacity = capacity;
        this.titaniumCost = titaniumCost;
        this.gasCost = gasCost;
        this.productionTime = productionTime;
    }

    public static ShipList findByName(String name){
        ShipList[] values = ShipList.values();
        for (ShipList value : values) {
            if(value.name.equals(name)){
                return value;
            }
        }
        return null;
    }

    private final String name;
    private final int attack;
    private final int maxHp;
    private final int capacity;
    private final int titaniumCost;
    private final int gasCost;
    private final int productionTime;


}
