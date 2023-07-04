package com.example.game.coordinate.repository.coordinate;

import com.example.game.coordinate.entity.Coordinate;
import com.example.game.coordinate.entity.QCoordinate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.example.game.coordinate.entity.QCoordinate.coordinate;

public class CoordinateRepositoryImpl implements CoordinateRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CoordinateRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Coordinate> findCoordinateByXY(Long x, Long y) {
        Coordinate coordinate1 = jpaQueryFactory
                .select(coordinate)
                .where(coordinate.xPos.eq(x), coordinate.yPos.eq(y))
                .fetchOne();

        return Optional.of(coordinate1);
    }
}
