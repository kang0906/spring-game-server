package com.example.game.fleet.service;

import com.example.game.fleet.entity.Fleet;
import com.example.game.fleet.entity.Ship;
import com.example.game.fleet.repository.ship.ShipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShipService {
    private final ShipRepository shipRepository;

    @Transactional
    public Ship makeScout(Fleet fleet,String name){
        Ship newShip = new Ship(fleet, name, "Scout", 10, 10, 10);
        newShip = shipRepository.save(newShip);
        return newShip;
    }

    @Transactional
    public Ship makeFrigate(Fleet fleet,String name){
        Ship newShip = new Ship(fleet, name, "Frigate", 100, 100, 100);
        newShip = shipRepository.save(newShip);
        return newShip;
    }

    @Transactional
    public Ship makeDestroyer(Fleet fleet,String name){
        Ship newShip = new Ship(fleet, name, "Destroyer", 1000, 1000, 1000);
        newShip = shipRepository.save(newShip);
        return newShip;
    }
}
