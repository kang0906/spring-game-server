package com.example.game.coordinate.service;

import com.example.game.coordinate.repository.coordinate.CoordinateRepository;
import com.example.game.coordinate.repository.infra.InfraRepository;
import com.example.game.coordinate.repository.item.ItemRepository;
import com.example.game.fleet.repository.fleet.FleetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CoordinateServiceBuildInfraTest {

    @Autowired
    private CoordinateRepository coordinateRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private InfraRepository infraRepository;
    @Autowired
    private FleetRepository fleetRepository;

    @Test
    public void coordinateServiceBuildInfraSuccessTest(){

    }
}
