package com.example.game.unit.repository;

import com.example.game.item.entity.ItemType;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitItem;
import com.example.game.unit.entity.UnitType;
import com.example.game.user.entity.User;
import com.example.game.user.repository.UserRepository;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.repository.WorldMapRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UnitItemRepositoryTest {

    @Autowired
    private UnitItemRepository unitItemRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorldMapRepository worldMapRepository;

    @DisplayName("유닛의 아이템을 정상적으로 저장한다.")
    @Test
    void unitItemSaveTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", 1L, 1L));
        Unit unit = unitRepository.save(new Unit(user, worldMap, "unitName", UnitType.INFANTRY));

        UnitItem save = unitItemRepository.save(new UnitItem(unit, ItemType.STEEL, 100));

        // when
        UnitItem unitItem = unitItemRepository.findWithPessimisticLockByUnitAndItemType(unit, ItemType.STEEL).get();

        // then
        assertThat(unitItem).isNotNull();
        assertThat(unitItem.getUnit().getUnitId()).isEqualTo(unit.getUnitId());
        assertThat(unitItem.getUnitItemId()).isEqualTo(save.getUnitItemId());
        assertThat(unitItem.getQuantity()).isEqualTo(100);
        assertThat(unitItem.getItemType()).isEqualTo(ItemType.STEEL);
    }

}
