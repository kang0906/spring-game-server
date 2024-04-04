package com.example.game.unit.repository;

import com.example.game.unit.entity.Unit;
import com.example.game.world.entity.WorldMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long>, UnitRepositoryCustom {
    Optional<Unit> findByWorldMap(WorldMap worldMap);
}
