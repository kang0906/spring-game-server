package com.example.game.facility.dto;

import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacilityResponseDto {

    private Long facilityId;
    private Long userId;
    private String name;
    private FacilityType type;
    private Long axisX;
    private Long axisY;

    public FacilityResponseDto(Facility facility) {
        this.facilityId = facility.getFacilityId();
        this.userId = facility.getUser().getUserId();
        this.name = facility.getName();
        this.type = facility.getType();
        this.axisX = facility.getAxisX();
        this.axisY = facility.getAxisY();
    }
}
