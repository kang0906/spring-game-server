package com.example.game.facility.repository;

import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.QFacility;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.example.game.facility.entity.QFacility.facility;


@Slf4j
public class FacilityRepositoryImpl implements FacilityRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public FacilityRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Facility> findAllByAxisBetween(long axisX, long axisY, int xRange, int yRange) {

        long xStart = axisX - xRange / 2;
        long xEnd = (xRange & 2) == 1 ? axisX + 1 + xRange / 2 : axisX + xRange / 2 ;
        long yStart = axisY - yRange / 2;
        long yEnd = (yRange & 2) == 1 ? axisY + 1 + yRange / 2 : axisY + yRange / 2 ;

        log.debug("findAllByAxisBetween ({}, {}) <-> ({}, {})", xStart, yStart, xEnd, yEnd);

        return queryFactory
                .selectFrom(facility)
                .where(
                        facility.axisX.between(xStart, xEnd),
                        facility.axisY.between(yStart, yEnd)
                )
                .fetch();
    }
}
