package com.example.game.fleet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShipModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipModuleId;

    private String moduleType;
}
