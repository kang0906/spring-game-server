package com.example.game.facility.dto;

import com.example.game.item.entity.ItemType;
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
    private ItemType itemType;
    private int quantity;
}
