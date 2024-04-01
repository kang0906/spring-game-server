package com.example.game.world.repository;

import com.example.game.world.entity.WorldMap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WorldMapRepositoryTest {

    @Autowired
    private WorldMapRepository worldMapRepository;

    @DisplayName("x좌표 값, y좌표 값을 이용하여 맵필드를 조회한다.")
    @Test
    void findByXAxisAndYAxisTest() {
        // given
        worldMapRepository.save(new WorldMap("테스트 필드", 1L, 1L));

        // when
        WorldMap worldMap = worldMapRepository.findByAxisXAndAxisY(1L, 1L);

        // then
        Assertions.assertThat(worldMap).isNotNull();
        Assertions.assertThat(worldMap.getAxisX()).isEqualTo(1L);
        Assertions.assertThat(worldMap.getAxisY()).isEqualTo(1L);

    }


}
