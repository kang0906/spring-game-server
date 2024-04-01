package com.example.game.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UnitServiceTest {

    @Autowired
    private UnitService unitService;

    @DisplayName("유닛을 정상적으로 이동한다.")
    @Test
    void unitMoveTest() {
        // given

        // when

        // then

    }

    @DisplayName("이미 다른 유닛이 있는 곳으로 이동을 시도할 경우 예외가 발생한다.")
    @Test
    void unitMoveNotEmptyFieldTest() {
        // given

        // when

        // then

    }

    @DisplayName("이동범위 밖으로 이동을 시도할 경우 예외가 발생한다.")
    @Test
    void unitMoveRangeTest() {
        // given

        // when

        // then

    }

    @DisplayName("소유하지 않은 유닛의 이동을 시도할 경우 예외가 발생한다.")
    @Test
    void unitMoveNotOwningTest() {
        // given

        // when

        // then

    }



}
