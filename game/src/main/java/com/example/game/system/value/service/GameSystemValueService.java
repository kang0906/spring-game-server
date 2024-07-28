package com.example.game.system.value.service;

import com.example.game.system.value.repository.GameSystemValueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GameSystemValueService {

    private final GameSystemValueRepository gameSystemValueRepository;

    public String getGameSystemValueByProperty(String property) {
        return gameSystemValueRepository.findByProperty(property).getGameSystemValue();
    }

    public int getGameSystemValueByPropertyParseInt(String property) {
        return Integer.parseInt(gameSystemValueRepository.findByProperty(property).getGameSystemValue());
    }

    public long getGameSystemValueByPropertyParseLong(String property) {
        return Long.parseLong(gameSystemValueRepository.findByProperty(property).getGameSystemValue());
    }
}
