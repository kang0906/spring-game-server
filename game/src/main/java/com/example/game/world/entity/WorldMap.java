package com.example.game.world.entity;

import com.example.game.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class WorldMap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long worldMapId;

    private String name;
    private Long x_axis;
    private Long y_axis;
    private LocalDateTime updateAt;

}
