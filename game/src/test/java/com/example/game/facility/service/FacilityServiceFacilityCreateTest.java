package com.example.game.facility.service;

import com.example.game.common.dto.ResponseDto;
import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.facility.dto.FacilityCreateRequestDto;
import com.example.game.facility.dto.FacilityResponseDto;
import com.example.game.facility.entity.Facility;
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

import static com.example.game.unit.entity.UnitType.INFANTRY;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class FacilityServiceFacilityCreateTest {

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

    @DisplayName("시설을 정상적으로 생성한다.")
    @Test
    void facilityCreateTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));
        unitItemRepository.save(new UnitItem(unit, ItemType.STEEL, 1000));

        // when
        facilityService.facilityCreate(user, new FacilityCreateRequestDto(unit.getUnitId(), "FARM"));

        // then
        Facility facility = facilityRepository.findByWorldMap(worldMap)
                .orElseThrow(RuntimeException::new);

    }

    @DisplayName("이미 시설이 있는곳에 생성을 시도할 경우 예외가 발생한다.")
    @Test
    void throwExceptionIfFacilityAlreadyExistTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));
        unitItemRepository.save(new UnitItem(unit, ItemType.STEEL, 1000));

        facilityService.facilityCreate(user, new FacilityCreateRequestDto(unit.getUnitId(), "FARM"));

        // when then
        assertThatThrownBy(() ->
                facilityService.facilityCreate(user, new FacilityCreateRequestDto(unit.getUnitId(), "FARM")))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.DESTINATION_NOT_EMPTY.getMessage());
    }

    @DisplayName("시설 생성에 필요한 아이템이 부족할 경우 예외 발생")
    @Test
    void throwExceptionIfNotEnoughItemTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));
        unitItemRepository.save(new UnitItem(unit, ItemType.STEEL, 0));

        // when then
        assertThatThrownBy(() ->
                facilityService.facilityCreate(user, new FacilityCreateRequestDto(unit.getUnitId(), "FARM")))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_ENOUGH_ITEM.getMessage());
    }

    @DisplayName("시설 생성에 필요한 아이템이 부족할 경우 예외 발생")
    @Test
    void throwExceptionIfNotEnoughItemTest2() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));

        // when then
        assertThatThrownBy(() ->
                facilityService.facilityCreate(user, new FacilityCreateRequestDto(unit.getUnitId(), "FARM")))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_ENOUGH_ITEM.getMessage());

    }

    @DisplayName("다른 유저의 유닛으로 시설생성을 시도할 경우 예외 발생")
    @Test
    void throwExceptionIfNotOwnUnitTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 2L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "", INFANTRY));
        unitItemRepository.save(new UnitItem(unit, ItemType.STEEL, 1000));

        User user2 = userRepository.save(new User("testUser2", null, "testUserName2", ""));

        // when then
        assertThatThrownBy(() ->
                facilityService.facilityCreate(user2, new FacilityCreateRequestDto(unit.getUnitId(), "FARM")))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.CANT_EDIT.getMessage());
    }
    

}
