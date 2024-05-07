package com.example.game.world.service;

import com.example.game.common.dto.ResponseDto;
import com.example.game.facility.entity.Facility;
import com.example.game.facility.repository.FacilityRepository;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.entity.User;
import com.example.game.world.dto.WorldMapFacilityResponseDto;
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
    private final FacilityRepository facilityRepository;

    public ResponseDto<WorldMapLoadResponseDto> loadWorldMap(User user, WorldMapLoadRequestDto requestDto) {

        int xRange = 18;
        int yRange = 9;

        List<Unit> allUnitByAxisBetween = unitRepository.findAllByAxisBetween(requestDto.getX(), requestDto.getY(), xRange, yRange);

        List<Facility> allFacilityByAxisBetween = facilityRepository.findAllByAxisBetween(requestDto.getX(), requestDto.getY(), xRange, yRange);
        // todo : 시설 정보 업데이트 로직 추가 (생산 완료 등 ...)

        WorldMapLoadResponseDto data = new WorldMapLoadResponseDto(
                user.getUserId(),
                allUnitByAxisBetween.stream()
                        .map(WorldMapUnitResponseDto::new)
                        .toList(),
                allFacilityByAxisBetween.stream()
                        .map(WorldMapFacilityResponseDto::new)
                        .toList()
        );

        return ResponseDto.success(data);
    }
}
