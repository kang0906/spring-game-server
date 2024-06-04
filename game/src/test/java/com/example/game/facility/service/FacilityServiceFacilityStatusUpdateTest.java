package com.example.game.facility.service;

import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityItem;
import com.example.game.facility.entity.FacilityType;
import com.example.game.facility.repository.FacilityItemRepository;
import com.example.game.facility.repository.FacilityRepository;
import com.example.game.item.entity.ItemType;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitItem;
import com.example.game.unit.entity.UnitType;
import com.example.game.unit.repository.UnitItemRepository;
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

import java.time.LocalDateTime;

import static com.example.game.unit.entity.UnitType.INFANTRY;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FacilityServiceFacilityStatusUpdateTest {
    @Autowired
    private FacilityService facilityService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private WorldMapRepository worldMapRepository;
    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private FacilityItemRepository facilityItemRepository;

    @DisplayName("아이템 생산 시설이 정상적으로 아이템을 생성한다.(이미 시설아이템 엔티티가 있을 때)")
    @CsvSource({"FARM,FOOD", "STEEL_FACTORY,STEEL"})
    @ParameterizedTest
    void itemFacilityStatusUpdateTest(FacilityType facilityType, ItemType itemType) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        LocalDateTime productionTime = LocalDateTime.now().minusHours(1).minusMinutes(1);
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", facilityType, productionTime));
        facilityItemRepository.save(new FacilityItem(itemType, 100, facility));

        // when
        facilityService.facilityStatusUpdate(facility);

        // then
        FacilityItem facilityItem = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, itemType)
                .orElseThrow(() -> new RuntimeException());
        assertThat(facility.getProductionTime()).isNotEqualTo(productionTime);

        assertThat(facilityItem).isNotNull();
        assertThat(facilityItem.getQuantity()).isEqualTo(200);
    }

    @DisplayName("아이템 생산 시설이 정상적으로 아이템을 생성한다.(시설아이템 엔티티가 없을 때)")
    @CsvSource({"FARM,FOOD", "STEEL_FACTORY,STEEL"})
    @ParameterizedTest
    void itemFacilityStatusUpdateTest2(FacilityType facilityType, ItemType itemType) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        LocalDateTime productionTime = LocalDateTime.now().minusHours(1).minusMinutes(1);
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", facilityType, productionTime));

        // when
        facilityService.facilityStatusUpdate(facility);

        // then
        FacilityItem facilityItem = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, itemType)
                .orElseThrow(() -> new RuntimeException());

        assertThat(facility.getProductionTime()).isNotEqualTo(productionTime);

        assertThat(facilityItem).isNotNull();
        assertThat(facilityItem.getQuantity()).isEqualTo(100);

    }

    @DisplayName("쿨타임이 돌지 않은 경우 아이템을 생산하지 않는다.")
    @CsvSource({"FARM,FOOD", "STEEL_FACTORY,STEEL"})
    @ParameterizedTest
    void itemFacilityNotCreateIfTest(FacilityType facilityType, ItemType itemType) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        LocalDateTime productionTime = LocalDateTime.now().minusMinutes(59);
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", facilityType, productionTime));
        facilityItemRepository.save(new FacilityItem(itemType, 100, facility));

        // when
        facilityService.facilityStatusUpdate(facility);

        // then
        FacilityItem facilityItem = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, itemType)
                .orElseThrow(() -> new RuntimeException());
        assertThat(facility.getProductionTime()).isEqualTo(productionTime);
        assertThat(facilityItem).isNotNull();
        assertThat(facilityItem.getQuantity()).isEqualTo(100);
    }

    @DisplayName("유닛 생산 시설이 정상적으로 유닛을 생성한다.")
    @CsvSource({"HEADQUARTERS,INFANTRY,100,100", "INFANTRY_SCHOOL,INFANTRY,100,100", "ARTILLERY_SCHOOL,ARTILLERY,150,100", "CAVALRY_SCHOOL,CAVALRY,200,100"})
    @ParameterizedTest
    void unitFacilityCreateUnitTest(FacilityType facilityType, UnitType unitType, int steelCost, int foodCost) {
        // given
        int itemCount = 1000;
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        LocalDateTime productionTime = LocalDateTime.now().minusMinutes(61);
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", facilityType, productionTime));
        FacilityItem facilitySteel = facilityItemRepository.save(new FacilityItem(ItemType.STEEL, itemCount, facility));
        FacilityItem facilityFood = facilityItemRepository.save(new FacilityItem(ItemType.FOOD, itemCount, facility));

        // when
        facilityService.facilityStatusUpdate(facility);

        // then
        Unit unit = unitRepository.findByWorldMap(worldMap)
                .orElseThrow(() -> new RuntimeException());
        assertThat(unit).isNotNull();
        assertThat(unit.getType()).isEqualTo(unitType);

        assertThat(facility.getProductionTime()).isNotEqualTo(productionTime);

        assertThat(facilitySteel.getQuantity()).isEqualTo(itemCount - steelCost);
        assertThat(facilityFood.getQuantity()).isEqualTo(itemCount - foodCost);
    }

    @DisplayName("생성 위치에 이미 유닛이 있는 경우 생산하지 않는다.")
    @CsvSource({"HEADQUARTERS,INFANTRY,100,100", "INFANTRY_SCHOOL,INFANTRY,100,100", "ARTILLERY_SCHOOL,ARTILLERY,150,100", "CAVALRY_SCHOOL,CAVALRY,200,100"})
    @ParameterizedTest
    void unitFacilityIfPositionNotEmptyTest(FacilityType facilityType, UnitType unitType, int steelCost, int foodCost) {
        // given
        int itemCount = 1000;
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        LocalDateTime productionTime = LocalDateTime.now().minusMinutes(61);
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", facilityType, productionTime));
        FacilityItem facilitySteel = facilityItemRepository.save(new FacilityItem(ItemType.STEEL, itemCount, facility));
        FacilityItem facilityFood = facilityItemRepository.save(new FacilityItem(ItemType.FOOD, itemCount, facility));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));


        // when
        facilityService.facilityStatusUpdate(facility);

        // then
        assertThat(facility.getProductionTime()).isEqualTo(productionTime);
        assertThat(facilitySteel.getQuantity()).isEqualTo(itemCount);
        assertThat(facilityFood.getQuantity()).isEqualTo(itemCount);

    }

    @DisplayName("유닛 생산에 필요한 아이템이 부족한 경우 생산하지 않는다.")
    @CsvSource({"HEADQUARTERS,INFANTRY,100,99", "INFANTRY_SCHOOL,INFANTRY,100,99", "ARTILLERY_SCHOOL,ARTILLERY,150,99", "CAVALRY_SCHOOL,CAVALRY,200,99",
            "HEADQUARTERS,INFANTRY,99,100", "INFANTRY_SCHOOL,INFANTRY,99,100", "ARTILLERY_SCHOOL,ARTILLERY,149,100", "CAVALRY_SCHOOL,CAVALRY,199,100"})
    @ParameterizedTest
    void unitFacilityIfNotEnoughItemTest(FacilityType facilityType, UnitType unitType, int steelCost, int foodCost) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        LocalDateTime productionTime = LocalDateTime.now().minusMinutes(61);
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", facilityType, productionTime));
        FacilityItem facilitySteel = facilityItemRepository.save(new FacilityItem(ItemType.STEEL, steelCost, facility));
        FacilityItem facilityFood = facilityItemRepository.save(new FacilityItem(ItemType.FOOD, foodCost, facility));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));


        // when
        facilityService.facilityStatusUpdate(facility);

        // then
        assertThat(facility.getProductionTime()).isEqualTo(productionTime);
        assertThat(facilitySteel.getQuantity()).isEqualTo(steelCost);
        assertThat(facilityFood.getQuantity()).isEqualTo(foodCost);

    }

    @DisplayName("쿨타임이 돌지 않은 경우 유닛을 생산하지 않는다.")
    @CsvSource({"HEADQUARTERS", "INFANTRY_SCHOOL", "ARTILLERY_SCHOOL", "CAVALRY_SCHOOL"})
    @ParameterizedTest
    void unitFacilityNotCreateIfTimeTest(FacilityType facilityType) {
        // given
        int itemCount = 1000;
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        LocalDateTime productionTime = LocalDateTime.now().minusMinutes(59);
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", facilityType, productionTime));
        FacilityItem facilitySteel = facilityItemRepository.save(new FacilityItem(ItemType.STEEL, itemCount, facility));
        FacilityItem facilityFood = facilityItemRepository.save(new FacilityItem(ItemType.FOOD, itemCount, facility));

        // when
        facilityService.facilityStatusUpdate(facility);

        // then
        assertThat(unitRepository.findByWorldMap(worldMap).isEmpty()).isTrue();
        assertThat(facility.getProductionTime()).isEqualTo(productionTime);

        assertThat(facilitySteel.getQuantity()).isEqualTo(itemCount);
        assertThat(facilityFood.getQuantity()).isEqualTo(itemCount);

    }

}
