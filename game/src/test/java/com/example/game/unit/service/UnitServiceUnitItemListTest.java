package com.example.game.unit.service;

import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityItem;
import com.example.game.facility.entity.FacilityType;
import com.example.game.facility.repository.FacilityItemRepository;
import com.example.game.facility.repository.FacilityRepository;
import com.example.game.item.entity.ItemType;
import com.example.game.system.value.entity.GameSystemValue;
import com.example.game.system.value.repository.GameSystemValueRepository;
import com.example.game.unit.dto.response.UnitDetailResponseDto;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitItem;
import com.example.game.unit.repository.UnitItemRepository;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.entity.User;
import com.example.game.user.repository.UserRepository;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.repository.WorldMapRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.example.game.unit.entity.UnitType.INFANTRY;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UnitServiceUnitItemListTest {

    @Autowired
    private UnitService unitService;

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
    @Autowired
    private GameSystemValueRepository gameSystemValueRepository;

    @BeforeEach
    void beforeAll() {
        gameSystemValueRepository.save(new GameSystemValue("game.user.new.map.clear.size", "100", ""));
        gameSystemValueRepository.save(new GameSystemValue("game.unit.cooldown", "30", ""));
        gameSystemValueRepository.save(new GameSystemValue("game.map.size", "25", ""));
    }

    @DisplayName("유닛의 아이템 목록을 조회한다.")
    @Test
    void unitItemListTest() {
        // given
        int moveQuantity = 100;
        int initialQuantity = 100;

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
        UnitDetailResponseDto responseDto = unitService.unitItemList(user, unit.getUnitId());

        // then
        assertThat(responseDto.getUnit().getUnitId()).isEqualTo(unit.getUnitId());
        assertThat(responseDto.getUnitItemList()).hasSize(2)
                .extracting("itemType", "quantity")
                .containsExactlyInAnyOrder(
                        tuple(ItemType.STEEL, initialQuantity),
                        tuple(ItemType.FOOD, initialQuantity)
                );

        assertThat(responseDto.getFacilityItemList()).hasSize(2)
                .extracting("itemType", "quantity")
                .containsExactlyInAnyOrder(
                        tuple(ItemType.STEEL, initialQuantity),
                        tuple(ItemType.FOOD, initialQuantity)
                );
    }
}
