package com.example.game.world.entity;

import com.example.game.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorldMap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long worldMapId;

    private String name;
    @Column(name = "axis_x")
    private Long axisX;
    @Column(name = "axis_y")
    private Long axisY;

    public WorldMap(String name, Long axisX, Long axisY) {
        this.name = name;
        this.axisX = axisX;
        this.axisY = axisY;
    }
}
