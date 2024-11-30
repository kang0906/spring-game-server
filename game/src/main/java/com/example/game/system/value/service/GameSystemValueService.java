package com.example.game.system.value.service;

import com.example.game.system.value.entity.GameSystemValue;
import com.example.game.system.value.repository.GameSystemValueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.BootstrapRegistryInitializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GameSystemValueService {

    private final GameSystemValueRepository gameSystemValueRepository;

    public String getGameSystemValueByProperty(String property) {

        GameSystemValue byProperty = gameSystemValueRepository.findByProperty(property);
        if (byProperty == null) {
           log.warn("can not find property : [{}]", property);
        }
        return byProperty.getGameSystemValue();
    }

    public int getGameSystemValueByPropertyParseInt(String property) {
        return Integer.parseInt(getGameSystemValueByProperty(property));
    }

    public long getGameSystemValueByPropertyParseLong(String property) {
        return Long.parseLong(getGameSystemValueByProperty(property));
    }

    public void changeGameSystemValue(String property, String newGameSystemValue) {
        GameSystemValue byProperty = gameSystemValueRepository.findByProperty(property);
        byProperty.changeGameSystemValue(newGameSystemValue);
    }
}
