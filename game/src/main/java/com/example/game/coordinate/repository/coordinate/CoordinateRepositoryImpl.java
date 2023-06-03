package com.example.game.coordinate.repository.coordinate;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class CoordinateRepositoryImpl implements CoordinateRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CoordinateRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }
}
