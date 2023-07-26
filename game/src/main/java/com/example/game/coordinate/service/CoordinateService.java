package com.example.game.coordinate.service;

import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.coordinate.dto.BuildInfraRequestDto;
import com.example.game.coordinate.entity.*;
import com.example.game.coordinate.repository.coordinate.CoordinateRepository;
import com.example.game.coordinate.repository.infra.InfraRepository;
import com.example.game.coordinate.repository.item.ItemRepository;
import com.example.game.fleet.entity.Fleet;
import com.example.game.fleet.repository.fleet.FleetRepository;
import com.example.game.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoordinateService {
    private final MessageSource messageSource;
    private final CoordinateRepository coordinateRepository;
    private final ItemRepository itemRepository;
    private final InfraRepository infraRepository;
    private final FleetRepository fleetRepository;
    private Random random = new Random();

    public Long getRandumLong(){
        Long mapSize = Long.parseLong(messageSource.getMessage("game.map.size", null, null));
        return random.nextLong(mapSize);
    }

    @Transactional
    public Coordinate makeCoordinateForNewUser(){
        Coordinate coordinate = null;
        Long x = getRandumLong();
        Long y = getRandumLong();

        while (coordinate != null) {
            x = getRandumLong();
            y = getRandumLong();
            coordinate = coordinateRepository.findCoordinateByXY(x, y).orElse(null);
        }

        coordinate = new Coordinate(x, y, new Resources(100000, 40000));
        coordinate = coordinateRepository.save(coordinate);

        //아이템(생산시설 건설용) 생성
        itemRepository.save(new Item(coordinate, "titanium", 10000));
        itemRepository.save(new Item(coordinate, "gas", 5000));

        return coordinate;
    }

    @Transactional
    public boolean buildInfra(User user, BuildInfraRequestDto requestDto){
        //좌표 소유여부 확인
        Coordinate coordinate = coordinateRepository.findById(requestDto.getCoordinate())
                .orElseThrow(() -> new GlobalException(ErrorCode.VALIDATION_FAIL));
        Fleet coordinateFleet = fleetRepository.findByCoordinate(coordinate);
        List<Infra> coordinateInfra = infraRepository.findAllByCoordinate(coordinate);
        List<Item> coordinateItems = itemRepository.findAllByCoordinate(coordinate);
        InfraList infraBuildRequestData = InfraList.findByName(requestDto.getInfraName());

        if (coordinateFleet.getUser().getUserId() != user.getUserId()) {
            throw new GlobalException(ErrorCode.CANT_EDIT);
        }
        //중복된 시설 확인
        for (Infra infra : coordinateInfra) {
            if(infra.getName().equals(requestDto.getInfraName())){
                throw new GlobalException(ErrorCode.VALIDATION_FAIL);
            }
        }
        //자원량 확인(락)
        for (Item item : coordinateItems) {
            if(item.getItemName().equals("titanium")){
                item.useItem(infraBuildRequestData.getTitaniumCost());
            }else if(item.getItemName().equals("gas")){
                item.useItem(infraBuildRequestData.getGasCost());
            }
        }
        //시설 생성
        Infra infra = new Infra(coordinate, infraBuildRequestData.getName());
        infraRepository.save(infra);
        return true;
    }
}
