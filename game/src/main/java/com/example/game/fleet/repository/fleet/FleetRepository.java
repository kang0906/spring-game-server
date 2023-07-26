package com.example.game.fleet.repository.fleet;

import com.example.game.coordinate.entity.Coordinate;
import com.example.game.fleet.entity.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FleetRepository extends JpaRepository<Fleet, Long>, FleetRepositoryCustom {
    public Fleet findByCoordinate(Coordinate coordinate);
}
