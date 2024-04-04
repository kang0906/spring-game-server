package com.example.game.unit.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UnitRepositoryTest {

    @Autowired
    private UnitRepository unitRepository;

    @DisplayName("좌표값을 통해 맵에 존재하는 유닛 목록을 정상적으로 조회한다.")
    @Test
    void findAllByAxisBetweenTest() {
        // given
//        unitRepository.save();

        // when
        unitRepository.findAllByAxisBetween(0, 0, 18, 9);

        // then

    }

}
