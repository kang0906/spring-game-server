package com.example.game.world.dto;

import com.example.game.facility.dto.FacilityResponseDto;
import com.example.game.unit.dto.UnitResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorldMapLoadResponseDto {

    private long userId;
    private List<UnitResponseDto> worldMapUnitDtoList;
    private List<FacilityResponseDto> worldMapFacilityList;
}
