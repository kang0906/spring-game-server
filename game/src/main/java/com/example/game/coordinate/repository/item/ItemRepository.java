package com.example.game.coordinate.repository.item;

import com.example.game.coordinate.entity.Coordinate;
import com.example.game.coordinate.entity.Item;
import com.example.game.coordinate.repository.infra.InfraRepositoryCustom;
import com.example.game.fleet.entity.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    public List<Item> findAllByCoordinate(Coordinate coordinate);

}
