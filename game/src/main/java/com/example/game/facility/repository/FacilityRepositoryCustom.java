package com.example.game.facility.repository;

import com.example.game.facility.entity.Facility;

import java.util.List;

public interface FacilityRepositoryCustom {
    List<Facility> findAllByAxisBetween(long axisX, long axisY, int xRange, int yRange);

}
