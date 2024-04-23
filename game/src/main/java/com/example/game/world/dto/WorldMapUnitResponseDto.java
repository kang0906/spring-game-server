package com.example.game.world.dto;

import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorldMapUnitResponseDto {
    private Long unitId;
    private Long userId;
    private String name;
    private UnitType type;
    private int hp;
    private int ap;
    private int dp;
    private Long axisX;
    private Long axisY;


    public WorldMapUnitResponseDto(Unit unit) {
        this.unitId = unit.getUnitId();
        this.userId = unit.getUser().getUserId();
        this.name = unit.getName();
        this.type = unit.getType();
        this.hp = unit.getHp();
        this.ap = unit.getAp();
        this.dp = unit.getDp();
        this.axisX = unit.getAxisX();
        this.axisY = unit.getAxisY();
    }
}
