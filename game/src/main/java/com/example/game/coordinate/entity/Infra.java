package com.example.game.coordinate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Infra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long infraId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinate_id")
    private Coordinate coordinate;

    private String name;
    private String infraInfo;
    private LocalDateTime updateAt;

    public Infra(Coordinate coordinate, String name){
        this.coordinate = coordinate;
        this.name = name;
    }

    public void requestCommend(String commend){
        this.infraInfo = commend;
        this.updateAt = LocalDateTime.now();
    }
}
