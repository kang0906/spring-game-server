package com.example.game.coordinate.repository.infra;

import com.example.game.coordinate.entity.Coordinate;
import com.example.game.coordinate.entity.Infra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfraRepository extends JpaRepository<Infra, Long>, InfraRepositoryCustom {

    List<Infra> findAllByCoordinate(Coordinate coordinate);
    Infra findAllByCoordinateAndName(Coordinate coordinate,String name);
}
