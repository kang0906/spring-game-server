package com.example.game.item.entity;


import com.example.game.common.entity.BaseEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor
public abstract class Item extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ItemType itemType;
    private int quantity;

    protected Item(ItemType itemType, int quantity) {
        this.itemType = itemType;
        this.quantity = quantity;
    }

    public int useItem(int quantity) {
        if (quantity < 0) {
            throw new RuntimeException("Item usage must be greater than 0");
        }
        this.quantity -= quantity;
        return this.quantity;
    }

    public int addItem(int quantity) {
        if (quantity < 0) {
            throw new RuntimeException("Item increase quantity must be greater than 0");
        }
        this.quantity += quantity;
        return this.quantity;
    }
}
