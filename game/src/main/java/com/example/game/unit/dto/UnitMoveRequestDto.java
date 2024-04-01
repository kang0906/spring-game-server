package com.example.game.unit.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UnitMoveRequestDto {

    private Long unitId;
    private Long x;
    private Long y;
}
