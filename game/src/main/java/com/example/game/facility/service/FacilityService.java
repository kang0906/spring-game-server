package com.example.game.facility.service;

import com.example.game.common.dto.ResponseDto;
import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.facility.dto.FacilityCreateRequestDto;
import com.example.game.facility.dto.FacilityResponseDto;
import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityType;
import com.example.game.facility.repository.FacilityRepository;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.entity.User;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.repository.WorldMapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.game.common.exception.ErrorCode.CANT_EDIT;
import static com.example.game.common.exception.ErrorCode.DATA_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final UnitRepository unitRepository;
    private final WorldMapRepository worldMapRepository;

    @Transactional
    public ResponseDto<FacilityResponseDto> facilityCreate(User user, FacilityCreateRequestDto requestDto) {


        Unit unit = unitRepository.findById(requestDto.getUnitId())
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));
        if (!unit.getUser().getUserId().equals(user.getUserId())) {
            throw new GlobalException(CANT_EDIT);
        }

        WorldMap worldMap = worldMapRepository.findByAxisXAndAxisY(unit.getAxisX(), unit.getAxisY());
        if (facilityRepository.findByWorldMap(worldMap).isPresent()) {
            throw new GlobalException(ErrorCode.DESTINATION_NOT_EMPTY);
        }

        Facility save = facilityRepository.save(new Facility(user, worldMap, requestDto.getFacilityType(), FacilityType.valueOf(requestDto.getFacilityType())));

        return ResponseDto.success(new FacilityResponseDto(save));
    }
}
