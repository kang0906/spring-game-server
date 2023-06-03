package com.example.game.coordinate.repository.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class InfraRepositoryImpl implements InfraRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public InfraRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }
}
