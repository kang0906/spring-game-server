package com.example.game.facility.service;

import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.facility.dto.FacilityCreateRequestDto;
import com.example.game.facility.dto.FacilityItemMoveRequestDto;
import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityItem;
import com.example.game.facility.entity.FacilityType;
import com.example.game.facility.repository.FacilityItemRepository;
import com.example.game.facility.repository.FacilityRepository;
import com.example.game.item.entity.ItemType;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitItem;
import com.example.game.unit.repository.UnitItemRepository;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.entity.User;
import com.example.game.user.repository.UserRepository;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.repository.WorldMapRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.game.unit.entity.UnitType.INFANTRY;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FacilityServiceItemMoveTest {

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
    private UnitItemRepository unitItemRepository;
    @Autowired
    private FacilityItemRepository facilityItemRepository;

    @DisplayName("시설의 아이템을 유닛으로 이동한다.")
    @Test
    void itemMoveTest() {
        // given
        int moveQuantity = 100;
        int initialQuantity = 1000;

        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", FacilityType.FARM));
        facilityItemRepository.save(new FacilityItem(ItemType.STEEL, initialQuantity, facility));
        facilityItemRepository.save(new FacilityItem(ItemType.FOOD, initialQuantity, facility));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));
        unitItemRepository.save(new UnitItem(unit, ItemType.STEEL, initialQuantity));
        unitItemRepository.save(new UnitItem(unit, ItemType.FOOD, initialQuantity));

        // when
        facilityService.facilityItemMove(user, new FacilityItemMoveRequestDto(facility.getFacilityId(), ItemType.STEEL, moveQuantity));

        // then
        List<FacilityItem> allByFacility = facilityItemRepository.findAllByFacility(facility);
        for (FacilityItem facilityItem : allByFacility) {
            if (facilityItem.getItemType().equals(ItemType.STEEL)) {
               assertThat(facilityItem.getQuantity()).isEqualTo(initialQuantity - moveQuantity);
            } else {
                assertThat(facilityItem.getQuantity()).isEqualTo(initialQuantity);
            }
        }

        List<UnitItem> allByUnit = unitItemRepository.findAllByUnit(unit);
        for (UnitItem unitItem : allByUnit) {
            if (unitItem.getItemType().equals(ItemType.STEEL)) {
                assertThat(unitItem.getQuantity()).isEqualTo(initialQuantity + moveQuantity);
            } else {
                assertThat(unitItem.getQuantity()).isEqualTo(initialQuantity);
            }
        }
    }

    @DisplayName("아이템이 옮겨지는 UnitItem 엔티티가 없을 때 시설의 아이템을 유닛으로 이동한다.")
    @Test
    void itemMoveTest2() {
        // given
        int moveQuantity = 100;
        int initialQuantity = 1000;

        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", FacilityType.FARM));
        facilityItemRepository.save(new FacilityItem(ItemType.STEEL, initialQuantity, facility));
        facilityItemRepository.save(new FacilityItem(ItemType.FOOD, initialQuantity, facility));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));

        // when
        facilityService.facilityItemMove(user, new FacilityItemMoveRequestDto(facility.getFacilityId(), ItemType.STEEL, moveQuantity));

        // then
        List<FacilityItem> allByFacility = facilityItemRepository.findAllByFacility(facility);
        for (FacilityItem facilityItem : allByFacility) {
            if (facilityItem.getItemType().equals(ItemType.STEEL)) {
                assertThat(facilityItem.getQuantity()).isEqualTo(initialQuantity - moveQuantity);
            } else {
                assertThat(facilityItem.getQuantity()).isEqualTo(initialQuantity);
            }
        }

        List<UnitItem> allByUnit = unitItemRepository.findAllByUnit(unit);
        for (UnitItem unitItem : allByUnit) {
            if (unitItem.getItemType().equals(ItemType.STEEL)) {
                assertThat(unitItem.getQuantity()).isEqualTo(moveQuantity);
            } else {
                assertThat(unitItem.getQuantity()).isEqualTo(0);
            }
        }
    }

    @DisplayName("아이템이 부족할 경우 예외가 발생한다.")
    @Test
    void ItemNotEnoughTest() {
        // given
        int moveQuantity = 100;
        int initialQuantity = 10;

        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", FacilityType.FARM));
        facilityItemRepository.save(new FacilityItem(ItemType.STEEL, initialQuantity, facility));
        facilityItemRepository.save(new FacilityItem(ItemType.FOOD, initialQuantity, facility));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));
        unitItemRepository.save(new UnitItem(unit, ItemType.STEEL, initialQuantity));
        unitItemRepository.save(new UnitItem(unit, ItemType.FOOD, initialQuantity));

        // when then
        assertThatThrownBy(() ->
                facilityService.facilityItemMove(user, new FacilityItemMoveRequestDto(facility.getFacilityId(), ItemType.STEEL, moveQuantity)))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_ENOUGH_ITEM.getMessage());
    }

    @DisplayName("아이템이 부족할 경우(아이템 엔티티가 없는 경우) 예외가 발생한다.")
    @Test
    void ItemNotEnoughTest2() {
        // given
        int moveQuantity = 100;
        int initialQuantity = 10;

        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", FacilityType.FARM));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));


        // when then
        assertThatThrownBy(() ->
                facilityService.facilityItemMove(user, new FacilityItemMoveRequestDto(facility.getFacilityId(), ItemType.STEEL, moveQuantity)))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_ENOUGH_ITEM.getMessage());
    }

    @DisplayName("소유하지 않은 시설의 경우 예외가 발생한다.")
    @Test
    void facilityItemMoveWithNotOwningTest() {
        // given
        int moveQuantity = 100;
        int initialQuantity = 10;

        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", FacilityType.FARM));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));

        // when then
        assertThatThrownBy(() ->
                facilityService.facilityItemMove(user2, new FacilityItemMoveRequestDto(facility.getFacilityId(), ItemType.STEEL, moveQuantity)))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.CANT_EDIT.getMessage());
    }
}
