package com.example.game.facility.entity;

import com.example.game.common.entity.BaseEntity;
import com.example.game.world.entity.WorldMap;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Facility extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "world_map_id")
    private WorldMap worldMap;

    private String name;
    private String type;    // todo : enum 사용
}
