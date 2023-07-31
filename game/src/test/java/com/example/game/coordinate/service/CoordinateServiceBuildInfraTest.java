package com.example.game.coordinate.service;

import com.example.game.common.exception.GlobalException;
import com.example.game.coordinate.dto.BuildInfraRequestDto;
import com.example.game.coordinate.entity.Coordinate;
import com.example.game.coordinate.entity.Infra;
import com.example.game.coordinate.entity.Item;
import com.example.game.coordinate.entity.Resources;
import com.example.game.coordinate.repository.coordinate.CoordinateRepository;
import com.example.game.coordinate.repository.infra.InfraRepository;
import com.example.game.coordinate.repository.item.ItemRepository;
import com.example.game.fleet.entity.Fleet;
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
class CoordinateServiceBuildInfraTest {

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
    }

    @Test
    @DisplayName("정상동작 테스트")
    public void coordinateServiceBuildInfraSuccessTest(){

        //given => BeforeEach

        //when
        boolean result = coordinateService.buildInfra(user, coordinate.getMapId(), new BuildInfraRequestDto("titaniumQuarry"));

        //then
        List<Item> allByCoordinate = itemRepository.findAllByCoordinate(coordinate);
        for (Item item : allByCoordinate) {
            if(item.getItemName().equals("titanium")){
                assertEquals(item.getAmount(),6000); // todo : messageSource 이용시 변경필요
            }
        }
        assertTrue(result);
        List<Infra> infraList = infraRepository.findAllByCoordinate(coordinate);
        assertEquals(infraList.size(),1);
        assertEquals(infraList.get(0).getName(),"titaniumQuarry");
    }
    @Test
    @DisplayName("자원부족시 예외발생 테스트")
    public void coordinateServiceBuildInfraNotEnoughItemExceptionTest(){
        //given
        List<Item> allByCoordinate = itemRepository.findAllByCoordinate(coordinate);
        for (Item item : allByCoordinate) {
            if(item.getItemName().equals("titanium")) {
                item.useItem(item.getAmount()-1);
            }
        }
        //when then
        assertThrows(GlobalException.class,()-> coordinateService.buildInfra(user,coordinate.getMapId(), new BuildInfraRequestDto("titaniumQuarry")));
    }

    @Test
    @DisplayName("중복건설시 예외발생 테스트")
    public void coordinateServiceBuildInfraDuplicateExceptionTest(){
        //given
        coordinateService.buildInfra(user,coordinate.getMapId(), new BuildInfraRequestDto("titaniumQuarry"));
        //when then
        assertThrows(GlobalException.class,()-> coordinateService.buildInfra(user,coordinate.getMapId(), new BuildInfraRequestDto("titaniumQuarry")));
    }

    @Test
    @DisplayName("권한없을시 예외발생 테스트")
    public void coordinateServiceBuildInfraNoAccessAuthorityTest(){
        Coordinate save = coordinateRepository.save(new Coordinate(coordinate.getXPos() + 1, 1L, new Resources(10000, 10000)));
        assertThrows(GlobalException.class,()-> coordinateService.buildInfra(user,save.getMapId(), new BuildInfraRequestDto("titaniumQuarry")));
    }
}
