package com.example.game.unit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitDetailResponseDto {
    private UnitResponseDto unit;
    private List<UnitItemResponseDto> unitItemList;
}
