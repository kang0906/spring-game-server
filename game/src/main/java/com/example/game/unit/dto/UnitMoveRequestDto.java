package com.example.game.unit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitMoveRequestDto {

    private Long unitId;
    private Long x;
    private Long y;
}
