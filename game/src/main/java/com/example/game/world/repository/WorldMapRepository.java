package com.example.game.world.repository;

import com.example.game.world.entity.WorldMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorldMapRepository extends JpaRepository<WorldMap, Long> {

    WorldMap findByAxisXAndAxisY(Long axisX, Long axisY);
}
