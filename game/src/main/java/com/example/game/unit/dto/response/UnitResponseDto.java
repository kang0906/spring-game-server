package com.example.game.unit.dto.response;

import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitResponseDto {
    private Long unitId;
    private Long userId;
    private String name;
    private UnitType type;
    private int hp;
    private int ap;
    private int dp;
    private Long axisX;
    private Long axisY;
    private LocalDateTime actionTime;

    public void makeUnknown() {
        this.name = "";
        this.hp = -1;
        this.ap = -1;
        this.dp = -1;
        this.actionTime = null;
    }

    public UnitResponseDto(Unit unit, int cooldown) {
        this.unitId = unit.getUnitId();
        this.userId = unit.getUser().getUserId();
        this.name = unit.getName();
        this.type = unit.getType();
        this.hp = unit.getHp();
        this.ap = unit.getAp();
        this.dp = unit.getDp();
        this.axisX = unit.getAxisX();
        this.axisY = unit.getAxisY();
        this.actionTime = unit.getActionTime().plusMinutes(cooldown);
    }
}
