package com.example.game.unit.entity;

import com.example.game.common.entity.BaseEntity;
import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.user.entity.User;
import com.example.game.world.entity.WorldMap;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Enumerated(EnumType.STRING)
    private UnitType type;
    private int hp;
    private int ap;
    private int dp;
    @Column(name = "axis_x")
    private Long axisX;
    @Column(name = "axis_y")
    private Long axisY;
    private LocalDateTime actionTime;


    public void move(WorldMap destination) {
        worldMap = destination;
        axisX = destination.getAxisX();
        axisY = destination.getAxisY();
        updateActionTime();
    }

    public void takeAttackFrom(Unit unit) {
        this.hp -= unit.ap;
    }

    public Unit(User user, WorldMap worldMap, String name, UnitType type, int hp, int ap, int dp) {
        this.user = user;
        this.worldMap = worldMap;
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.ap = ap;
        this.dp = dp;
        this.axisX = worldMap.getAxisX();
        this.axisY = worldMap.getAxisY();
        this.actionTime = LocalDateTime.now().minusDays(1);
    }

    public Unit(User user, WorldMap worldMap, String name, UnitType type) {
        this.user = user;
        this.worldMap = worldMap;
        this.name = name;
        this.type = type;
        this.hp = type.getMaxHp();
        this.ap = type.getAp();
        this.dp = type.getDp();
        this.axisX = worldMap.getAxisX();
        this.axisY = worldMap.getAxisY();
        this.actionTime = LocalDateTime.now().minusDays(1);
    }

    public void updateActionTime() {
        this.actionTime = LocalDateTime.now().withNano(0);
    }

    public void checkActionTime(int coolDownTimeMinutes) {
        if (actionTime.isAfter(LocalDateTime.now().minusMinutes(coolDownTimeMinutes))) {
           throw new GlobalException(ErrorCode.NEED_COOL_DOWN_ERROR);
        }
    }
}
