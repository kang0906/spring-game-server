package com.example.game.system.value.service;

import com.example.game.system.value.entity.GameSystemValue;
import com.example.game.system.value.repository.GameSystemValueRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GameSystemValueServiceTest {

    @Autowired
    private GameSystemValueService gameSystemValueService;
    @Autowired
    private GameSystemValueRepository gameSystemValueRepository;

    @DisplayName("게임 시스템 변수를 정상적으로 조회한다.")
    @Test
    void gameSystemValueServiceTest() {
        // given
        gameSystemValueRepository.save(new GameSystemValue("game.user.new.map.clear.size", "100", ""));
        gameSystemValueRepository.save(new GameSystemValue("game.unit.cooldown", "30", ""));
        gameSystemValueRepository.save(new GameSystemValue("game.map.size", "25", ""));

        // when
        String clearSize = gameSystemValueService.getGameSystemValueByProperty("game.user.new.map.clear.size");
        String cooldown = gameSystemValueService.getGameSystemValueByProperty("game.unit.cooldown");
        String mapSize = gameSystemValueService.getGameSystemValueByProperty("game.map.size");

        int clearSizeInt = gameSystemValueService.getGameSystemValueByPropertyParseInt("game.user.new.map.clear.size");
        int cooldownInt = gameSystemValueService.getGameSystemValueByPropertyParseInt("game.unit.cooldown");

        long mapSizeLong = gameSystemValueService.getGameSystemValueByPropertyParseLong("game.map.size");

        // then
        assertThat(clearSize).isNotNull();
        assertThat(cooldown).isNotNull();
        assertThat(mapSize).isNotNull();

        assertThat(Integer.parseInt(clearSize)).isEqualTo(clearSizeInt);
        assertThat(Integer.parseInt(cooldown)).isEqualTo(cooldownInt);

        assertThat(Long.parseLong(mapSize)).isEqualTo(mapSizeLong);

    }

}
