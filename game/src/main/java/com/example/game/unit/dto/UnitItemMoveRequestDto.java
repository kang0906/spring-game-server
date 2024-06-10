package com.example.game.unit.dto;

import com.example.game.item.entity.ItemType;
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
    private ItemType itemType;
    private int quantity;
}
