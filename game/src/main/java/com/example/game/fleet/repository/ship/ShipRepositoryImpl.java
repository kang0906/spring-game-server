package com.example.game.fleet.repository.ship;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class ShipRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;

    public ShipRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


}
