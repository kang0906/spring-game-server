package com.example.game.item.entity;


import com.example.game.common.entity.BaseEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Item extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ItemType name;
    private int quantity;
}
