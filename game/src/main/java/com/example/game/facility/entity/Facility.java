package com.example.game.facility.entity;

import com.example.game.common.entity.BaseEntity;
import com.example.game.user.entity.User;
import com.example.game.world.entity.WorldMap;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Facility extends BaseEntity {

    // todo : 구현 (엔티티 코드 구현 시 상속을 사용할지 고려해볼것 : 다형성)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "world_map_id")
    private WorldMap worldMap;

    private String name;

    @Enumerated(EnumType.STRING)
    private FacilityType type;

    @Column(name = "axis_x")
    private Long axisX;
    @Column(name = "axis_y")
    private Long axisY;

    private LocalDateTime productionTime;

    public Facility(User user,WorldMap worldMap, String name, FacilityType type) {
        this.user = user;
        this.worldMap = worldMap;
        this.name = name;
        this.type = type;
        this.axisX = worldMap.getAxisX();
        this.axisY = worldMap.getAxisY();
        this.productionTime = LocalDateTime.now();
    }

    public Facility(User user,WorldMap worldMap, String name, FacilityType type, LocalDateTime productionTime) {
        this.user = user;
        this.worldMap = worldMap;
        this.name = name;
        this.type = type;
        this.axisX = worldMap.getAxisX();
        this.axisY = worldMap.getAxisY();
        this.productionTime = productionTime;
    }

    public void updateProductionTime() {
        this.productionTime = LocalDateTime.now();
    }

    public void changeUser(User user) {
        this.user = user;
    }
}
