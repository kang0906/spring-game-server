package com.example.game.facility.controller;

import com.example.game.common.dto.ResponseDto;
import com.example.game.config.UserDetailsImpl;
import com.example.game.facility.dto.FacilityCreateRequestDto;
import com.example.game.facility.dto.FacilityItemMoveRequestDto;
import com.example.game.facility.dto.FacilityResponseDto;
import com.example.game.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @ResponseBody
    @PostMapping("/facility/create")
    public ResponseDto<FacilityResponseDto> facilityCreate(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FacilityCreateRequestDto requestDto) {

        return ResponseDto.success(facilityService.facilityCreate(userDetails.getUser(), requestDto));
    }

    @ResponseBody
    @PostMapping("/facility/item/move")
    public ResponseDto<String> facilityItemMove(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FacilityItemMoveRequestDto requestDto) {

        return ResponseDto.success(facilityService.facilityItemMove(userDetails.getUser(), requestDto));
    }
}
