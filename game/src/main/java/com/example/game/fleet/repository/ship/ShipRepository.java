package com.example.game.fleet.repository.ship;

import com.example.game.fleet.entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship, Long>, ShipRepositoryCustom {
}
