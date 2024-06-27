package com.example.game.unit.dto.response;

import com.example.game.facility.entity.FacilityItem;
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
public class ItemResponseDto {
    private ItemType itemType;
    private int quantity;

    public ItemResponseDto(UnitItem unitItem) {
        this.itemType = unitItem.getItemType();
        this.quantity = unitItem.getQuantity();
    }

    public ItemResponseDto(FacilityItem unitItem) {
        this.itemType = unitItem.getItemType();
        this.quantity = unitItem.getQuantity();
    }
}
