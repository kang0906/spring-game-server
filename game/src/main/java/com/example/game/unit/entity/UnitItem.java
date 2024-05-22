package com.example.game.unit.entity;

import com.example.game.item.entity.Item;
import com.example.game.item.entity.ItemType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UnitItem extends Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unitItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    public UnitItem(Unit unit, ItemType itemType, int quantity) {
        super(itemType, quantity);
        this.unit = unit;
    }
}
