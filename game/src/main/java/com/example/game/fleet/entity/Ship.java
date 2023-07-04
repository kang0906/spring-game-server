package com.example.game.fleet.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;

    private String shipName;
    private String type;

    private int attack;
    private int hp;
    private int maxHp;
    private int capacity;
//    private int fuel;

    public Ship(Fleet fleet, String shipName, String type, int attack, int maxHp, int capacity) {
        this.fleet = fleet;
        this.shipName = shipName;
        this.type = type;
        this.attack = attack;
        this.hp = maxHp;
        this.maxHp = maxHp;
        this.capacity = capacity;
    }
}
