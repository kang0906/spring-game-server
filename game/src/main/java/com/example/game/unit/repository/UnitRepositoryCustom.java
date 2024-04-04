package com.example.game.unit.repository;

import com.example.game.unit.entity.Unit;

import java.util.List;

public interface UnitRepositoryCustom {

    List<Unit> findAllByAxisBetween(long axisX, long axisY, int xRange, int yRange);

}
