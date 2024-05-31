package com.example.game.facility.repository;

import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityItem;
import com.example.game.facility.entity.FacilityType;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FacilityItemRepositoryTest {
    @Autowired
    private FacilityItemRepository facilityItemRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private WorldMapRepository worldMapRepository;

    @DisplayName("유닛의 아이템을 정상적으로 저장한다.")
    @Test
    void facilityItemSaveTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 1L));
        Facility facility = facilityRepository.save(new Facility(user, worldMap, "facilityName", FacilityType.FARM));

        FacilityItem save = facilityItemRepository.save(new FacilityItem(ItemType.STEEL, 100, facility));

        // when
        FacilityItem facilityItem = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, ItemType.STEEL).get();

        // then
        assertThat(facilityItem).isNotNull();
        assertThat(facilityItem.getFacility().getFacilityId()).isEqualTo(facility.getFacilityId());
        assertThat(facilityItem.getFacilityItemId()).isEqualTo(save.getFacilityItemId());
        assertThat(facilityItem.getQuantity()).isEqualTo(100);
        assertThat(facilityItem.getItemType()).isEqualTo(ItemType.STEEL);
    }
}
