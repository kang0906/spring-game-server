package com.example.game.unit.dto.response;

import com.example.game.item.entity.ItemType;
import com.example.game.unit.entity.UnitItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitItemResponseDto {
    private ItemType itemType;
    private int quantity;

    public UnitItemResponseDto(UnitItem unitItem) {
        this.itemType = unitItem.getItemType();
        this.quantity = unitItem.getQuantity();
    }
}
