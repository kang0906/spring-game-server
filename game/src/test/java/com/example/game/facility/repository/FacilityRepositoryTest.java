package com.example.game.facility.repository;

import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityType;
import com.example.game.user.entity.User;
import com.example.game.user.repository.UserRepository;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.repository.WorldMapRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FacilityRepositoryTest {
    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorldMapRepository worldMapRepository;

    @DisplayName("좌표값을 통해 맵에 존재하는 유닛들을 목록을 정상적으로 조회한다.")
    @Test
    public void findAllByAxisBetweenTest() {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));

        Facility facility1 = makeFacility(user, 1L, 2L, "facility1", FacilityType.HEADQUARTERS);
        Facility facility2 = makeFacility(user, 100L, 100L, "facility2", FacilityType.HEADQUARTERS);
        Facility facility3 = makeFacility(user, -9L, 4L, "facility3", FacilityType.HEADQUARTERS);

        // when
        List<Facility> allByAxisBetween = facilityRepository.findAllByAxisBetween(0, 0, 18, 9);

        // then
        assertThat(allByAxisBetween.size()).isEqualTo(2);
        assertThat(allByAxisBetween).extracting("name", String.class)
                .contains("facility1","facility3");
    }

    @DisplayName("좌표값을 통해 맵에 존재하는 유닛들을 목록을 조회할때 경계값을 정상적으로 조회한다.")
    @CsvSource({"9,4", "9,-4", "-9,4", "-9,-4"})
    @ParameterizedTest
    void findAllByAxisBetweenBoundaryTest(long x, long y) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));

        Facility facility1 = makeFacility(user, x, y, "facility1", FacilityType.HEADQUARTERS);

        // when
        List<Facility> allByAxisBetween = facilityRepository.findAllByAxisBetween(0, 0, 18, 9);

        // then
        assertThat(allByAxisBetween.size()).isEqualTo(1);
        assertThat(allByAxisBetween).extracting("name", String.class)
                .contains("facility1");
    }

    @DisplayName("좌표값을 통해 맵에 존재하는 유닛들을 목록을 조회할때 경계 밖의 값은 조회되지 않는다.")
    @CsvSource({"10,5", "10,-5", "-10,5", "-10,-5"})
    @ParameterizedTest
    void findAllByAxisBetweenOutOfBoundaryTest(long x, long y) {
        // given
        User user = userRepository.save(new User("testUser", null, "testUserName", ""));
        Facility facility1 = makeFacility(user, x, y, "facility1", FacilityType.HEADQUARTERS);

        // when
        List<Facility> allByAxisBetween = facilityRepository.findAllByAxisBetween(0, 0, 18, 9);

        // then
        assertThat(allByAxisBetween.size()).isEqualTo(0);
    }


    private Facility makeFacility(User user, long x, long y, String facilityName, FacilityType facilityType) {
        WorldMap worldMap = worldMapRepository.save(new WorldMap("", x, y));
        return facilityRepository.save(new Facility(user, worldMap, facilityName, facilityType));
    }
}
