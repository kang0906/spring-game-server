package com.example.game.coordinate.service;

import com.example.game.common.exception.GlobalException;
import com.example.game.coordinate.dto.BuildInfraRequestDto;
import com.example.game.coordinate.dto.BuildShipRequestDto;
import com.example.game.coordinate.entity.*;
import com.example.game.coordinate.repository.coordinate.CoordinateRepository;
import com.example.game.coordinate.repository.infra.InfraRepository;
import com.example.game.coordinate.repository.item.ItemRepository;
import com.example.game.fleet.entity.Fleet;
import com.example.game.fleet.entity.ShipList;
import com.example.game.fleet.repository.fleet.FleetRepository;
import com.example.game.user.entity.User;
import com.example.game.user.repository.UserRepository;
import com.example.game.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CoordinateServiceBuildShipTest {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CoordinateRepository coordinateRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private InfraRepository infraRepository;
    @Autowired
    private FleetRepository fleetRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoordinateService coordinateService;
    @Autowired
    private UserService userService;

    private Coordinate coordinate;
    private Fleet fleet;
    private User user;

    @BeforeEach
    void beforeEach() {
        user = new User("testUserEmail", 1L, "testUser", "testUser");
        user = userRepository.save(this.user);

        userService.setNewUser(user);

        List<Fleet> byUser = fleetRepository.findByUser(user);
        fleet = byUser.get(0);
        coordinate = fleet.getCoordinate();
        infraRepository.save(new Infra(coordinate, InfraList.SHIP_YARD.getName()));
    }

    @Test
    @DisplayName("정상동작 테스트")
    public void coordinateServiceBuildShipSuccessTest(){
        coordinateService.buildShip(user, coordinate.getMapId(), new BuildShipRequestDto(ShipList.FRIGATE.getName()));
        List<Item> allByCoordinate = itemRepository.findAllByCoordinate(coordinate);

        for (Item item : allByCoordinate) {
            if(item.getItemName().equals("titanium")){
                assertEquals(item.getAmount(),10000 - ShipList.FRIGATE.getTitaniumCost()); // todo : messageSource 이용시 변경필요
            }
            if(item.getItemName().equals("gas")){
                assertEquals(item.getAmount(),5000 - ShipList.FRIGATE.getGasCost()); // todo : messageSource 이용시 변경필요
            }
            Infra infra = infraRepository.findAllByCoordinateAndName(coordinate, InfraList.SHIP_YARD.getName());
            assertEquals(infra.getInfraInfo(), ShipList.FRIGATE.getName());
            assertNotNull(infra.getUpdateAt());
        }
    }

    @Test
    @DisplayName("권한없을시 예외발생 테스트(좌표 미소유)")
    public void coordinateServiceBuildShipNoAccessAuthorityTest(){
        Coordinate save = coordinateRepository.save(new Coordinate(coordinate.getXPos() + 1, 1L, new Resources(10000, 10000)));
        assertThrows(GlobalException.class,()-> coordinateService.buildShip(user, save.getMapId(), new BuildShipRequestDto(ShipList.FRIGATE.getName())));
    }

    @Test
    @DisplayName("ship yard 시설 존재 여부 확인")
    public void coordinateServiceBuildShipWithNoShipyardTest(){
        Infra infra = infraRepository.findAllByCoordinateAndName(coordinate, InfraList.SHIP_YARD.getName());
        infraRepository.delete(infra);
        assertThrows(GlobalException.class,()-> coordinateService.buildShip(user, coordinate.getMapId(), new BuildShipRequestDto(ShipList.FRIGATE.getName())));

    }

    @Test
    @DisplayName("자원부족시 예외발생 테스트")
    public void coordinateServiceBuildShipNotEnoughItemExceptionTest(){
        //given
        List<Item> allByCoordinate = itemRepository.findAllByCoordinate(coordinate);
        for (Item item : allByCoordinate) {
            if(item.getItemName().equals("titanium")) {
                item.useItem(item.getAmount()-1);
            }
            if(item.getItemName().equals("gas")) {
                item.useItem(item.getAmount()-1);
            }
        }
        //when then
        assertThrows(GlobalException.class,()-> coordinateService.buildShip(user, coordinate.getMapId(), new BuildShipRequestDto(ShipList.FRIGATE.getName())));
    }

    //이미 생상중인 시설에서 생산시도시 예외발생 테스트
    @Test
    @DisplayName("이미 생상중인 시설에서 생산시도시 예외발생 테스트")
    public void coordinateServiceBuildShipDuplicateExceptionTest(){
        coordinateService.buildShip(user, coordinate.getMapId(), new BuildShipRequestDto(ShipList.FRIGATE.getName()));
        assertThrows(GlobalException.class,()-> coordinateService.buildShip(user, coordinate.getMapId(), new BuildShipRequestDto(ShipList.FRIGATE.getName())));
    }
}
