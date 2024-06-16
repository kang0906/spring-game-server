package com.example.game.unit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitAttackResponseDto {
    private UnitResponseDto attackUnit;
    private UnitResponseDto targetUnit;
}
