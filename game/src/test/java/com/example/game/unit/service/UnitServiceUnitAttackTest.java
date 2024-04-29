package com.example.game.unit.service;

import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.unit.dto.UnitAttackRequestDto;
import com.example.game.unit.dto.UnitMoveRequestDto;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.entity.User;
import com.example.game.user.repository.UserRepository;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.repository.WorldMapRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.example.game.unit.entity.UnitType.INFANTRY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UnitServiceUnitAttackTest {


    @Autowired
    private UnitService unitService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private WorldMapRepository worldMapRepository;

    @DisplayName("유닛을 정상적으로 공격한다.")
    @Test
    void unitAttackTest() {
        // given
        User user = userRepository.save(new User("testUser1", null, "testUserName1", ""));
        WorldMap worldMap1 = worldMapRepository.save(new WorldMap("", -1L, -2L));
        Unit unit1 = unitRepository.save(
                new Unit(user, worldMap1, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));
        WorldMap worldMap2 = worldMapRepository.save(
                new WorldMap("", worldMap1.getAxisX() + 1, worldMap1.getAxisY()));

        Unit unit2 = unitRepository.save(
                new Unit(user2, worldMap2, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        // when
        unitService.unitAttack(new UnitAttackRequestDto(unit1.getUnitId(), unit2.getUnitId()), user);

        // then
        Unit findUnit = unitRepository.findById(unit2.getUnitId()).orElseThrow(() -> new RuntimeException("테스트 실패"));
        assertThat(findUnit.getHp()).isEqualTo(INFANTRY.getMaxHp() - INFANTRY.getAp());
    }

    @DisplayName("유닛을 정상적으로 공격한다.")
    @CsvSource({"0,0", "0,1", "1,0", "1,1", "0, -1", "-1, 0", "-1,-1", "1, -1", "-1,1"})
    @ParameterizedTest
    void unitAttackTestUseParameterize(Long x, Long y) {
        // given
        User user = userRepository.save(new User("testUser1", null, "testUserName1", ""));
        WorldMap worldMap1 = worldMapRepository.save(new WorldMap("", x, y));
        Unit unit1 = unitRepository.save(
                new Unit(user, worldMap1, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));
        WorldMap worldMap2 = worldMapRepository.save(
                new WorldMap("", worldMap1.getAxisX() + 1, worldMap1.getAxisY()));

        Unit unit2 = unitRepository.save(
                new Unit(user2, worldMap2, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        // when
        unitService.unitAttack(new UnitAttackRequestDto(unit1.getUnitId(), unit2.getUnitId()), user);

        // then
        Unit findUnit = unitRepository.findById(unit2.getUnitId()).orElseThrow(() -> new RuntimeException("테스트 실패"));
        assertThat(findUnit.getHp()).isEqualTo(INFANTRY.getMaxHp() - INFANTRY.getAp());
    }

    @DisplayName("공격범위 밖의 유닛을 공격하려고 시도할 경우 예외가 발생한다.")
    @CsvSource({"0,2", "2,0", "2,2", "0, -2", "-2, 0", "-2,-2", "2, -2", "-2,2"})
    @ParameterizedTest
    void unitAttackRangeParameterizedTest(Long x, Long y) {
        User user = userRepository.save(new User("testUser1", null, "testUserName1", ""));
        WorldMap worldMap1 = worldMapRepository.save(new WorldMap("", 0L, 0L));
        Unit unit1 = unitRepository.save(
                new Unit(user, worldMap1, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));
        WorldMap worldMap2 = worldMapRepository.save(
                new WorldMap("", worldMap1.getAxisX() + x , worldMap1.getAxisY() + y));

        Unit unit2 = unitRepository.save(
                new Unit(user2, worldMap2, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        // when then
        assertThatThrownBy(() -> unitService.unitAttack(new UnitAttackRequestDto(unit1.getUnitId(), unit2.getUnitId()), user))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.OUT_OF_RANGE.getMessage());
    }

    @DisplayName("소유하지 않은 유닛으로 공격을 시도할 경우 예외가 발생한다.")
    @Test
    void unitAttackWithNotOwningTest() {
        // given
        User user = userRepository.save(new User("testUser1", null, "testUserName1", ""));
        WorldMap worldMap1 = worldMapRepository.save(new WorldMap("", -1L, -2L));
        Unit unit1 = unitRepository.save(
                new Unit(user, worldMap1, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));
        WorldMap worldMap2 = worldMapRepository.save(
                new WorldMap("", worldMap1.getAxisX() + 1, worldMap1.getAxisY()));

        Unit unit2 = unitRepository.save(
                new Unit(user2, worldMap2, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        // when then
        assertThatThrownBy(() -> unitService.unitAttack(new UnitAttackRequestDto(unit1.getUnitId(), unit2.getUnitId()), user2))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.CANT_EDIT.getMessage());
    }

    @DisplayName("자신의 유닛을 공격하려고 시도할 경우 예외가 발생한다.")
    @Test
    void unitAttackFriendlyTest() {
        // given
        User user = userRepository.save(new User("testUser1", null, "testUserName1", ""));
        WorldMap worldMap1 = worldMapRepository.save(new WorldMap("", -1L, -2L));
        Unit unit1 = unitRepository.save(
                new Unit(user, worldMap1, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        WorldMap worldMap2 = worldMapRepository.save(
                new WorldMap("", worldMap1.getAxisX() + 1, worldMap1.getAxisY()));

        Unit unit2 = unitRepository.save(
                new Unit(user, worldMap2, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        // when then
        assertThatThrownBy(() -> unitService.unitAttack(new UnitAttackRequestDto(unit1.getUnitId(), unit2.getUnitId()), user))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.FRIENDLY_FIRE_NOT_ALLOWED.getMessage());
    }
}
