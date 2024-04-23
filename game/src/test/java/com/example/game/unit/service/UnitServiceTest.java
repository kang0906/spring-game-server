package com.example.game.unit.service;

import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.unit.dto.UnitMoveRequestDto;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitType;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.entity.User;
import com.example.game.user.repository.UserRepository;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.repository.WorldMapRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.example.game.unit.entity.UnitType.INFANTRY;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UnitServiceTest {

    @Autowired
    private UnitService unitService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private WorldMapRepository worldMapRepository;

    @DisplayName("유닛을 정상적으로 이동한다.")
    @Test
    void unitMoveTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY, 100, 10, 1));

        // when
        boolean result = unitService.unitMove(new UnitMoveRequestDto(unit.getUnitId(), 1L, 1L), user);

        // then
        Unit findUnit = unitRepository.findById(unit.getUnitId()).orElseThrow(() -> new RuntimeException("테스트 실패"));
        assertThat(result).isTrue();
        assertThat(findUnit.getWorldMap().getAxisX()).isEqualTo(2L);
        assertThat(findUnit.getWorldMap().getAxisY()).isEqualTo(3L);
    }

    @DisplayName("유닛을 정상적으로 이동한다.")
    @CsvSource({"0,0", "0,1", "1,0", "1,1", "0, -1", "-1, 0", "-1,-1", "1, -1", "-1,1"})
    @ParameterizedTest
    void unitMoveTestUseParameterize(Long moveX, Long moveY) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 0L, 0L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY, 100, 10, 1));

        // when
        boolean result = unitService.unitMove(new UnitMoveRequestDto(unit.getUnitId(), moveX, moveY), user);

        // then
        Unit findUnit = unitRepository.findById(unit.getUnitId()).orElseThrow(() -> new RuntimeException("테스트 실패"));
        assertThat(result).isTrue();
        assertThat(findUnit.getWorldMap().getAxisX()).isEqualTo(moveX);
        assertThat(findUnit.getWorldMap().getAxisY()).isEqualTo(moveY);
    }

    @DisplayName("이미 다른 유닛이 있는 곳으로 이동을 시도할 경우 예외가 발생한다.")
    @Test
    void unitMoveNotEmptyFieldTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));

        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        WorldMap worldMap2 = worldMapRepository.save(new WorldMap("", 2L, 3L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY, 100, 10, 1));
        Unit unit2 = unitRepository.save(new Unit(user, worldMap2, "", INFANTRY, 100, 10, 1));

        // when then
        assertThatThrownBy(() -> unitService.unitMove(new UnitMoveRequestDto(unit.getUnitId(), 1L, 1L), user))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.DESTINATION_NOT_EMPTY.getMessage());

    }

    @DisplayName("이동범위 밖으로 이동을 시도할 경우 예외가 발생한다.")
    @CsvSource({"0,2", "2,0", "2,2", "0, -2", "-2, 0", "-2,-2", "2, -2", "-2,2"})
    @ParameterizedTest
    void unitMoveRangeParameterizedTest(Long moveX, Long moveY) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY, 100, 10, 1));

        // when then
        assertThatThrownBy(() -> unitService.unitMove(new UnitMoveRequestDto(unit.getUnitId(), moveX, moveY), user))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.VALIDATION_FAIL.getMessage());
    }

    @DisplayName("x 좌표의 이동범위 밖으로 이동을 시도할 경우 예외가 발생한다.")
    @Test
    void unitMoveRangeTest1() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY, 100, 10, 1));

        // when then
        assertThatThrownBy(() -> unitService.unitMove(new UnitMoveRequestDto(unit.getUnitId(), 2L, 1L), user))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.VALIDATION_FAIL.getMessage());
    }

    @DisplayName("y 좌표의 이동범위 밖으로 이동을 시도할 경우 예외가 발생한다.")
    @Test
    void unitMoveRangeTest2() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY, 100, 10, 1));

        // when then
        assertThatThrownBy(() -> unitService.unitMove(new UnitMoveRequestDto(unit.getUnitId(), 1L, 2L), user))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.VALIDATION_FAIL.getMessage());
    }

    @DisplayName("소유하지 않은 유닛의 이동을 시도할 경우 예외가 발생한다.")
    @Test
    void unitMoveNotOwningTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Unit unit = unitRepository.save(new Unit(user2, worldMap, "", INFANTRY, 100, 10, 1));

        // when then
        assertThatThrownBy(() -> unitService.unitMove(new UnitMoveRequestDto(unit.getUnitId(), 1L, 1L), user))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.CANT_EDIT.getMessage());
    }



}
