package com.example.game.unit.repository;

import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitType;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UnitRepositoryTest {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorldMapRepository worldMapRepository;

    @DisplayName("좌표값을 통해 맵에 존재하는 유닛들을 목록을 정상적으로 조회한다.")
    @Test
    public void findAllByAxisBetweenTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));

        Unit unit1 = makeUnit(user, 1L, 2L, "unit1");
        Unit unit2 = makeUnit(user, 100L, 100L, "unit2");
        Unit unit3 = makeUnit(user, -9L, 4L, "unit3");


        // when
        List<Unit> allByAxisBetween = unitRepository.findAllByAxisBetween(0, 0, 18, 9);

        // then
        assertThat(allByAxisBetween.size()).isEqualTo(2);
        assertThat(allByAxisBetween).extracting("name", String.class)
                .contains("unit1","unit3");
    }

    @DisplayName("좌표값을 통해 맵에 존재하는 유닛들을 목록을 조회할때 경계값을 정상적으로 조회한다.")
    @CsvSource({"9,4", "9,-4", "-9,4", "-9,-4"})
    @ParameterizedTest
    void findAllByAxisBetweenBoundaryTest(long x, long y) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        Unit unit1 = makeUnit(user, x, y, "unit1");

        // when
        List<Unit> allByAxisBetween = unitRepository.findAllByAxisBetween(0, 0, 18, 9);

        // then
        assertThat(allByAxisBetween.size()).isEqualTo(1);
        assertThat(allByAxisBetween).extracting("name", String.class)
                .contains("unit1");
    }

    @DisplayName("좌표값을 통해 맵에 존재하는 유닛들을 목록을 조회할때 경계 밖의 값은 조회되지 않는다.")
    @CsvSource({"10,5", "10,-5", "-10,5", "-10,-5"})
    @ParameterizedTest
    void findAllByAxisBetweenOutOfBoundaryTest(long x, long y) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        Unit unit1 = makeUnit(user, x, y, "unit1");

        // when
        List<Unit> allByAxisBetween = unitRepository.findAllByAxisBetween(0, 0, 18, 9);

        // then
        assertThat(allByAxisBetween.size()).isEqualTo(0);
    }


    private Unit makeUnit(User user, long x, long y, String unitName) {
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", x, y));
        return unitRepository.save(new Unit(user, worldMap, unitName, UnitType.INFANTRY, 100, 10, 1));
    }

}
