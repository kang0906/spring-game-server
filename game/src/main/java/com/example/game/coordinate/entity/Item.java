package com.example.game.coordinate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinate_id")
    private Coordinate coordinate;

    private String itemName;
    private int amount;

    public Item(Coordinate coordinate, String itemName, int amount) {
        this.coordinate = coordinate;
        this.itemName = itemName;
        this.amount = amount;
    }
}
