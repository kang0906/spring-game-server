package com.example.game.coordinate.repository.coordinate;

import com.example.game.coordinate.entity.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinateRepository extends JpaRepository<Coordinate, Long>, CoordinateRepositoryCustom {
}
