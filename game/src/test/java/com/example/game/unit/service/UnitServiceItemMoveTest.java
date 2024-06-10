package com.example.game.unit.service;

import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityItem;
import com.example.game.facility.entity.FacilityType;
import com.example.game.facility.repository.FacilityItemRepository;
import com.example.game.facility.repository.FacilityRepository;
import com.example.game.facility.service.FacilityService;
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

import static com.example.game.unit.entity.UnitType.INFANTRY;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UnitServiceItemMoveTest {

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

    @DisplayName("유닛의 아이템을 시설로 이동한다.")
    @Test
    void itemMoveTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", FacilityType.FARM));
        facilityItemRepository.save(new FacilityItem(ItemType.STEEL, 1000, facility));
        facilityItemRepository.save(new FacilityItem(ItemType.FOOD, 1000, facility));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));
        unitItemRepository.save(new UnitItem(unit, ItemType.STEEL, 1000));
        unitItemRepository.save(new UnitItem(unit, ItemType.FOOD, 1000));

        // when

        // then

    }

    @DisplayName("아이템이 옮겨지는 FacilityItem  엔티티가 없을 때 유닛의 아이템을 시설로 이동한다.")
    @Test
    void itemMoveTest2() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", FacilityType.FARM));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));
        unitItemRepository.save(new UnitItem(unit, ItemType.STEEL, 1000));
        unitItemRepository.save(new UnitItem(unit, ItemType.FOOD, 1000));

        // when

        // then

    }

    @DisplayName("아이템이 부족할 경우 예외가 발생한다.")
    @Test
    void ItemNotEnoughTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Facility facility = facilityRepository
                .save(new Facility(user, worldMap, "testFacility", FacilityType.FARM));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));
        unitItemRepository.save(new UnitItem(unit, ItemType.STEEL, 1000));
        unitItemRepository.save(new UnitItem(unit, ItemType.FOOD, 1000));


        // when

        // then

    }
}
