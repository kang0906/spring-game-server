package com.example.game.world.service;

import com.example.game.common.dto.ResponseDto;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.entity.User;
import com.example.game.world.dto.WorldMapLoadRequestDto;
import com.example.game.world.dto.WorldMapLoadResponseDto;
import com.example.game.world.dto.WorldMapUnitResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorldMapService {

    private final UnitRepository unitRepository;

    public ResponseDto<WorldMapLoadResponseDto> loadWorldMap(User user, WorldMapLoadRequestDto requestDto) {


        List<Unit> allByAxisBetween = unitRepository.findAllByAxisBetween(requestDto.getX(), requestDto.getY(), 18, 9);


        WorldMapLoadResponseDto data = new WorldMapLoadResponseDto(
                user.getUserId(),
                allByAxisBetween.stream()
                        .map(unit -> new WorldMapUnitResponseDto(unit))
                        .toList()
        );

        return ResponseDto.success(data);
    }
}
