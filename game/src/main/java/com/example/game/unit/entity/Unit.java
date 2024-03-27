package com.example.game.unit.entity;

import com.example.game.common.entity.BaseEntity;
import com.example.game.user.entity.User;
import com.example.game.world.entity.WorldMap;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Unit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "world_map_id")
    private WorldMap worldMap;

    private String name;
    private String type;
    private int hp;
    private int ap;
    private int dp;

}
