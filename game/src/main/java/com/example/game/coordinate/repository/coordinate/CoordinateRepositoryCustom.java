package com.example.game.coordinate.repository.coordinate;

import com.example.game.coordinate.entity.Coordinate;

import java.util.Optional;

public interface CoordinateRepositoryCustom {
    public Optional<Coordinate> findCoordinateByXY(Long x, Long y);
}
