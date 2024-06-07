package com.example.game.unit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitItemMoveRequestDto {

    private Long unitId;
    private int steelQuantity;
    private int foodQuantity;
}
