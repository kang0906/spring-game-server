package com.example.game.fleet.service;

import com.example.game.coordinate.entity.Coordinate;
import com.example.game.fleet.entity.Fleet;
import com.example.game.fleet.entity.Ship;
import com.example.game.fleet.repository.fleet.FleetRepository;
import com.example.game.fleet.repository.ship.ShipRepository;
import com.example.game.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class FleetService {

    private final FleetRepository fleetRepository;
    private final ShipRepository shipRepository;
    private final ShipService shipService;

    private Random random = new Random();


    @Transactional
    public void makeFleetForNewUser(User user, Coordinate coordinate) {
        Fleet newFleet = new Fleet(user, coordinate, getRandomFleetName());
        newFleet = fleetRepository.save(newFleet);

        for(int i=1;i<=15;i++){
            if(i<10){
                shipService.makeScout(newFleet, "정찰선00"+i);
            }else{
                shipService.makeScout(newFleet, "정찰선0"+i);
            }
        }

        for(int i=1;i<=10;i++){
            if(i<10){
                shipService.makeFrigate(newFleet, "호위함00"+i);
            }else{
                shipService.makeFrigate(newFleet, "호위함0"+i);
            }
        }

        shipService.makeDestroyer(newFleet, "구축함001");
        shipService.makeDestroyer(newFleet, "구축함002");

    }

    private String getRandomFleetName(){
        int fleetNum = random.nextInt(9999);
        return fleetNum + "함대";
    }
}
