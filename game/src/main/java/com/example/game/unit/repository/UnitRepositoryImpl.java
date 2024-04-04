package com.example.game.unit.repository;

import com.example.game.unit.entity.Unit;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.example.game.unit.entity.QUnit.unit;

@Slf4j
public class UnitRepositoryImpl implements UnitRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UnitRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Unit> findAllByAxisBetween(long axisX, long axisY, int xRange, int yRange) {

        long xStart = axisX - xRange / 2;
        long xEnd = (xRange & 2) == 1 ? axisX + 1 + xRange / 2 : axisX + xRange / 2 ;
        long yStart = axisY - yRange / 2;
        long yEnd = (yRange & 2) == 1 ? axisY + 1 + yRange / 2 : axisY + yRange / 2 ;

        log.debug("findAllByAxisBetween ({}, {}) <-> ({}, {})", xStart, yStart, xEnd, yEnd);

        return queryFactory
                .selectFrom(unit)
                .where(
                        unit.axisX.between(xStart, xEnd),
                        unit.axisY.between(yStart, yEnd)
                )
                .fetch();
    }
}
