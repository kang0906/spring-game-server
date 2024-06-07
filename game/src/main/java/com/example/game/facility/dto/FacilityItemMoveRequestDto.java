package com.example.game.facility.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacilityItemMoveRequestDto {

    private Long facilityId;
    private int steelQuantity;
    private int foodQuantity;
}
