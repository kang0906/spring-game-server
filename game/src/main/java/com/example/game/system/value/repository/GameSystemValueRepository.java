package com.example.game.system.value.repository;

import com.example.game.system.value.entity.GameSystemValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSystemValueRepository extends JpaRepository<GameSystemValue, Long> {

    GameSystemValue findByProperty(String property);
}
