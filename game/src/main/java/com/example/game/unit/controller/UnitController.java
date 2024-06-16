package com.example.game.unit.controller;

import com.example.game.common.dto.ResponseDto;
import com.example.game.config.UserDetailsImpl;
import com.example.game.unit.dto.request.UnitAttackRequestDto;
import com.example.game.unit.dto.request.UnitItemMoveRequestDto;
import com.example.game.unit.dto.request.UnitMoveRequestDto;
import com.example.game.unit.dto.response.UnitAttackResponseDto;
import com.example.game.unit.dto.response.UnitDetailResponseDto;
import com.example.game.unit.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @ResponseBody
    @PostMapping("/unit/move")
    public ResponseDto<String> unitMove(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UnitMoveRequestDto requestDto) {

        unitService.unitMove(requestDto, userDetails.getUser());

        return ResponseDto.success("success");
    }

    @ResponseBody
    @PostMapping("/unit/attack")
    public ResponseDto<UnitAttackResponseDto> unitAttack(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UnitAttackRequestDto requestDto) {

        return ResponseDto.success(unitService.unitAttack(requestDto, userDetails.getUser()));
    }

    @ResponseBody
    @PostMapping("/unit/item/move")
    public ResponseDto<String> unitItemMove(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UnitItemMoveRequestDto requestDto) {

        return ResponseDto.success(unitService.unitItemMove(userDetails.getUser(), requestDto));
    }

    @ResponseBody
    @GetMapping("/unit/{unitId}/item")
    public ResponseDto<UnitDetailResponseDto> unitItemList(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long unitId) {

        return ResponseDto.success(unitService.unitItemList(userDetails.getUser(), unitId));
    }
}
