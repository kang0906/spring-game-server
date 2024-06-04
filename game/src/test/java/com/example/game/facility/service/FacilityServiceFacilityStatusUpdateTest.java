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
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", facilityType, LocalDateTime.now().minusHours(1).minusMinutes(1)));
        facilityItemRepository.save(new FacilityItem(itemType, 100, facility));

        // when
        facilityService.facilityStatusUpdate(facility);

        // then
        FacilityItem facilityItem = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, itemType)
                .orElseThrow(() -> new RuntimeException());
        Assertions.assertThat(facilityItem).isNotNull();
        Assertions.assertThat(facilityItem.getQuantity()).isEqualTo(200);
    }

    @DisplayName("아이템 생산 시설이 정상적으로 아이템을 생성한다.(시설아이템 엔티티가 없을 때)")
    @CsvSource({"FARM,FOOD", "STEEL_FACTORY,STEEL"})
    @ParameterizedTest
    void itemFacilityStatusUpdateTest2(FacilityType facilityType, ItemType itemType) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", facilityType, LocalDateTime.now().minusHours(1).minusMinutes(1)));

        // when
        facilityService.facilityStatusUpdate(facility);

        // then
        FacilityItem facilityItem = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, itemType)
                .orElseThrow(() -> new RuntimeException());
        Assertions.assertThat(facilityItem).isNotNull();
        Assertions.assertThat(facilityItem.getQuantity()).isEqualTo(100);

    }

    @DisplayName("쿨타임이 돌지 않은 경우 아이템을 생산하지 않는다.")
    @CsvSource({"FARM,FOOD", "STEEL_FACTORY,STEEL"})
    @ParameterizedTest
    void itemFacilityNotCreateIfTest(FacilityType facilityType, ItemType itemType) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", facilityType, LocalDateTime.now().minusMinutes(59)));
        facilityItemRepository.save(new FacilityItem(itemType, 100, facility));

        // when
        facilityService.facilityStatusUpdate(facility);

        // then
        FacilityItem facilityItem = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, itemType)
                .orElseThrow(() -> new RuntimeException());
        Assertions.assertThat(facilityItem).isNotNull();
        Assertions.assertThat(facilityItem.getQuantity()).isEqualTo(100);
    }

    @DisplayName("유닛 생산 시설이 정상적으로 유닛을 생성한다.")
    @CsvSource({"HEADQUARTERS,INFANTRY", "INFANTRY_SCHOOL,INFANTRY", "ARTILLERY_SCHOOL,ARTILLERY", "CAVALRY_SCHOOL,CAVALRY"})
    @ParameterizedTest
    void unitFacilityCreateUnitTest(FacilityType facilityType, UnitType unitType) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", facilityType, LocalDateTime.now().minusMinutes(61)));
        facilityItemRepository.save(new FacilityItem(ItemType.STEEL, 1000, facility));
        facilityItemRepository.save(new FacilityItem(ItemType.FOOD, 1000, facility));

        // when

        // then

    }

    @DisplayName("생성 위치에 이미 유닛이 있는 경우 생산하지 않는다.")
    @Test
    void unitFacilityIfPositionNotEmptyTest() {
        // given

        // when

        // then

    }

    @DisplayName("생성 위치에 이미 유닛이 있는 경우 생산하지 않는다.")
    @Test
    void unitFacilityIfNotEnoughItemTest() {
        // given

        // when

        // then

    }

}
