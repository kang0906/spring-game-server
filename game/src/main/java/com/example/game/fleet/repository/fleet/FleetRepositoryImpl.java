package com.example.game.fleet.repository.fleet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class FleetRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;

    public FleetRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


}
