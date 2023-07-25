package com.example.game.coordinate.service;

import com.example.game.coordinate.entity.Coordinate;
import com.example.game.coordinate.entity.Item;
import com.example.game.coordinate.entity.Resources;
import com.example.game.coordinate.repository.coordinate.CoordinateRepository;
import com.example.game.coordinate.repository.item.ItemRepository;
import com.example.game.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoordinateService {
    private final MessageSource messageSource;
    private final CoordinateRepository coordinateRepository;
    private final ItemRepository itemRepository;
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
    public boolean buildInfra(User user){
        //좌표 소유여부 확인
        //자원량 확인(락)
        //자원감소
        //시설 생성
        return true;
    }
}
