package com.example.game.world.service;

import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.facility.entity.Facility;
import com.example.game.facility.repository.FacilityRepository;
import com.example.game.facility.service.FacilityService;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.entity.User;
import com.example.game.facility.dto.FacilityResponseDto;
import com.example.game.world.dto.WorldMapInfoDto;
import com.example.game.world.dto.WorldMapLoadRequestDto;
import com.example.game.world.dto.WorldMapLoadResponseDto;
import com.example.game.unit.dto.response.UnitResponseDto;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.repository.WorldMapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class WorldMapService {

    private final WorldMapRepository worldMapRepository;
    private final UnitRepository unitRepository;
    private final FacilityRepository facilityRepository;
    private final FacilityService facilityService;
    private final MessageSource messageSource;

    @Transactional
    public WorldMapLoadResponseDto loadWorldMap(User user, WorldMapLoadRequestDto requestDto) {

        int xRange = 18;
        int yRange = 9;

        List<Facility> allFacilityByAxisBetween = facilityRepository.findAllByAxisBetween(requestDto.getX(), requestDto.getY(), xRange, yRange);

        // todo : 시설 정보 업데이트 로직 추가 (생산 완료 등 ...)
        allFacilityByAxisBetween.stream().forEach(facilityService::facilityStatusUpdate);


        List<Unit> allUnitByAxisBetween = unitRepository.findAllByAxisBetween(requestDto.getX(), requestDto.getY(), xRange, yRange);

        WorldMapLoadResponseDto data = new WorldMapLoadResponseDto(
                user.getUserId(),
                allUnitByAxisBetween.stream()
                        .map(UnitResponseDto::new)
                        .toList(),
                allFacilityByAxisBetween.stream()
                        .map(FacilityResponseDto::new)
                        .toList()
        );

        List<UnitResponseDto> worldMapUnitDtoList = data.getWorldMapUnitDtoList();

        for (UnitResponseDto worldMapUnitResponseDto : worldMapUnitDtoList) {
            if (!user.getUserId().equals(worldMapUnitResponseDto.getUserId())) {
                worldMapUnitResponseDto.makeUnknown();
            }
        }

        return data;
    }

    public WorldMap getOrMakeWorldMap(long x, long y) {
        WorldMap destination = worldMapRepository
                .findByAxisXAndAxisY(x, y);

        if (destination == null) {
            destination = worldMapRepository.save(
                    new WorldMap("", x, y));
        }

        return destination;
    }

    public WorldMap findSpawnPosition(String userId) {
        Long maxMapSize = Long.parseLong(messageSource.getMessage("game.map.size", null, null));
        int mapClearSize = Integer.parseInt(messageSource.getMessage("game.user.new.map.clear.size", null, null));
        log.info("");

        Random random = new Random();
        Long x;
        Long y;

        for (int i = 0; i < 25; i++) {
            x = random.nextLong(maxMapSize);
            y = random.nextLong(maxMapSize);

            if (unitRepository.findAllByAxisBetween(x, y, mapClearSize, mapClearSize).isEmpty()
                    && facilityRepository.findAllByAxisBetween(x, y, mapClearSize, mapClearSize).isEmpty()) {
                log.info("WorldMapService.findSpawnPosition() 유저 [{}] 생성 위치 = ({}, {}) 탐색 시도 횟수 : {}, maxMapSize = {}, mapClearSize = {}"
                        , userId, x, y, i + 1, maxMapSize, mapClearSize);
                return getOrMakeWorldMap(x, y);
            }

            if (i == 5) {
                log.warn("유저 [{}] 생성 위치 탐색 시도 5회 초과...", userId);
                // todo : alert 발생
            }
        }

        throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public WorldMapInfoDto getWorldMapInfo() {
        return new WorldMapInfoDto(
                Long.parseLong(messageSource.getMessage("game.map.size", null, null))
        );
    }
}
