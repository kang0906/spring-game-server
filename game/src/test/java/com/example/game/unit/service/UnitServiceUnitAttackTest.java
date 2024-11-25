package com.example.game.unit.service;

import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.item.entity.ItemType;
import com.example.game.system.value.entity.GameSystemValue;
import com.example.game.system.value.repository.GameSystemValueRepository;
import com.example.game.system.value.service.GameSystemValueService;
import com.example.game.unit.dto.request.UnitAttackRequestDto;
import com.example.game.unit.dto.request.UnitMoveRequestDto;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitItem;
import com.example.game.unit.repository.UnitItemRepository;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.entity.User;
import com.example.game.user.repository.UserRepository;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.repository.WorldMapRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.game.unit.entity.UnitType.INFANTRY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @Autowired
    private UnitItemRepository unitItemRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private GameSystemValueRepository gameSystemValueRepository;

    @BeforeEach
    void beforeAll() {
        gameSystemValueRepository.save(new GameSystemValue("game.user.new.map.clear.size", "100", ""));
        gameSystemValueRepository.save(new GameSystemValue("game.unit.cooldown", "30", ""));
        gameSystemValueRepository.save(new GameSystemValue("game.map.size", "25", ""));
    }

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
                new Unit(user2, worldMap2, "", INFANTRY, INFANTRY.getAp() + 1, INFANTRY.getAp(), INFANTRY.getDp()));

        // when
        unitService.unitAttack(new UnitAttackRequestDto(unit1.getUnitId(), unit2.getUnitId()), user);

        // then
        Unit findUnit = unitRepository.findById(unit2.getUnitId()).orElseThrow(() -> new RuntimeException("테스트 실패"));
        assertThat(findUnit.getHp()).isEqualTo(INFANTRY.getAp() + 1 - INFANTRY.getAp());
    }

    @DisplayName("공격을 받은 유닛의 체력이 0 이하가 되면 해당 유닛은 삭제된다.")
    @Test
    void unitAttackAndTargetDeleteTest() {
        // given
        User user = userRepository.save(new User("testUser1", null, "testUserName1", ""));
        WorldMap worldMap1 = worldMapRepository.save(new WorldMap("", -1L, -2L));
        Unit unit1 = unitRepository.save(
                new Unit(user, worldMap1, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));
        WorldMap worldMap2 = worldMapRepository.save(
                new WorldMap("", worldMap1.getAxisX() + 1, worldMap1.getAxisY()));

        Unit unit2 = unitRepository.save(
                new Unit(user2, worldMap2, "", INFANTRY, INFANTRY.getAp(), INFANTRY.getAp(), INFANTRY.getDp()));

        // when
        unitService.unitAttack(new UnitAttackRequestDto(unit1.getUnitId(), unit2.getUnitId()), user);

        // then
        Unit findUnit = unitRepository.findById(unit2.getUnitId()).orElse(null);
        assertThat(findUnit).isNull();
    }

    @DisplayName("공격 당한 유닛이 죽으면 죽인 유저의 킬포인트가 증가한다.")
    @Test
    void addKillPointTest() {
        // given

        // when

        // then

    }

    
    @DisplayName("공격을 받은 유닛이 아이템을 가지고 있고 체력이 0 이하가 되면 해당 유닛은 삭제된다.")
    @Test
    void unitAttackAndTargetDeleteThatTargetHavingItemTest() {
        // given
        User user = userRepository.save(new User("testUser1", null, "testUserName1", ""));
        WorldMap worldMap1 = worldMapRepository.save(new WorldMap("", -1L, -2L));
        Unit unit1 = unitRepository.save(
                new Unit(user, worldMap1, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));
        unitItemRepository.save(new UnitItem(unit1, ItemType.STEEL, 100));


        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));
        WorldMap worldMap2 = worldMapRepository.save(
                new WorldMap("", worldMap1.getAxisX() + 1, worldMap1.getAxisY()));

        Unit unit2 = unitRepository.save(
                new Unit(user2, worldMap2, "", INFANTRY, INFANTRY.getAp(), INFANTRY.getAp(), INFANTRY.getDp()));
        unitItemRepository.save(new UnitItem(unit2, ItemType.STEEL, 100));
        unitItemRepository.save(new UnitItem(unit2, ItemType.FOOD, 100));

        // when
        unitService.unitAttack(new UnitAttackRequestDto(unit1.getUnitId(), unit2.getUnitId()), user);

        // then
        Unit findUnit = unitRepository.findById(unit2.getUnitId()).orElse(null);
        assertThat(findUnit).isNull();

        List<UnitItem> allByUnit = unitItemRepository.findAllByUnit(unit1);
        if (allByUnit.isEmpty()) {
            Assertions.fail();
        }
        for (UnitItem unitItem : allByUnit) {
            if (unitItem.getItemType().equals(ItemType.STEEL)) {
                assertThat(unitItem.getQuantity()).isEqualTo(200);
            } else if (unitItem.getItemType().equals(ItemType.FOOD)) {
                assertThat(unitItem.getQuantity()).isEqualTo(100);
            } else {
                Assertions.fail();
            }
        }
        entityManager.flush();
    }

    @DisplayName("유닛을 정상적으로 공격한다.")
    @CsvSource({"0,1", "1,0", "0, -1", "-1, 0"})
    @ParameterizedTest
    void unitAttackTestUseParameterize(Long x, Long y) {
        // given
        User user = userRepository.save(new User("testUser1", null, "testUserName1", ""));
        WorldMap worldMap1 = worldMapRepository.save(new WorldMap("", x, y));
        Unit unit1 = unitRepository.save(
                new Unit(user, worldMap1, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));
        WorldMap worldMap2 = worldMapRepository.save(
                new WorldMap("", worldMap1.getAxisX() + x, worldMap1.getAxisY() + y));

        Unit unit2 = unitRepository.save(
                new Unit(user2, worldMap2, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        // when
        unitService.unitAttack(new UnitAttackRequestDto(unit1.getUnitId(), unit2.getUnitId()), user);

        // then
        Unit findUnit = unitRepository.findById(unit2.getUnitId()).orElseThrow(() -> new RuntimeException("테스트 실패"));
        assertThat(findUnit.getHp()).isEqualTo(INFANTRY.getMaxHp() - INFANTRY.getAp());
    }

    @DisplayName("공격범위 밖의 유닛을 공격하려고 시도할 경우 예외가 발생한다.")
    @CsvSource({"0,2", "2,0", "2,2", "0, -2", "-2, 0", "-2,-2", "2, -2", "-2,2", "-1,-1", "1, -1", "-1,1", "1,1"})
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

    @DisplayName("유닛 쿨타임이 지나지 않았을 경우 예외가 발생한다.")
    @Test
    void unitMoveCoolDownTimeExceptionTest() {
        // given
        User user = userRepository.save(new User("testUser1", null, "testUserName1", ""));
        WorldMap worldMap1 = worldMapRepository.save(new WorldMap("", -1L, -2L));
        Unit unit1 = unitRepository.save(
                new Unit(user, worldMap1, "", INFANTRY, INFANTRY.getMaxHp(), INFANTRY.getAp(), INFANTRY.getDp()));

        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));
        WorldMap worldMap2 = worldMapRepository.save(
                new WorldMap("", worldMap1.getAxisX() + 1, worldMap1.getAxisY()));

        Unit unit2 = unitRepository.save(
                new Unit(user2, worldMap2, "", INFANTRY, INFANTRY.getAp() + 1, INFANTRY.getAp(), INFANTRY.getDp()));

        unitService.unitAttack(new UnitAttackRequestDto(unit1.getUnitId(), unit2.getUnitId()), user);

        // when then
        assertThatThrownBy(() -> unitService.unitAttack(new UnitAttackRequestDto(unit1.getUnitId(), unit2.getUnitId()), user))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NEED_COOL_DOWN_ERROR.getMessage());
    }
}
