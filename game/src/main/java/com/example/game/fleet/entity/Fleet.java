package com.example.game.fleet.entity;

import com.example.game.coordinate.entity.Coordinate;
import com.example.game.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fleet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fleetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinate_id")
    private Coordinate coordinate;

    private String fleetName;


}
