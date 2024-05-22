package com.example.game.facility.entity;

import com.example.game.item.entity.Item;
import com.example.game.item.entity.ItemType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacilityItem extends Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    public FacilityItem(ItemType itemType, int quantity, Facility facility) {
        super(itemType, quantity);
        this.facility = facility;
    }
}
