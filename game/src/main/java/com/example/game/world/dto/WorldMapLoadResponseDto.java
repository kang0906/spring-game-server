package com.example.game.world.dto;

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
    private List<WorldMapUnitResponseDto> worldMapUnitDtoList;
    private List<WorldMapFacilityResponseDto> worldMapFacilityList;
}
