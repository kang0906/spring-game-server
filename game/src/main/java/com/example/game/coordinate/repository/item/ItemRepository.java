package com.example.game.coordinate.repository.item;

import com.example.game.coordinate.entity.Item;
import com.example.game.coordinate.repository.infra.InfraRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
}
