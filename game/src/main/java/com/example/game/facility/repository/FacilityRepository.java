package com.example.game.facility.repository;

import com.example.game.facility.entity.Facility;
import com.example.game.unit.entity.Unit;
import com.example.game.world.entity.WorldMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long>, FacilityRepositoryCustom {

    Optional<Facility> findByWorldMap(WorldMap worldMap);
}
