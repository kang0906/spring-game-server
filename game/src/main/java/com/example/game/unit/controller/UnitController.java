package com.example.game.unit.controller;

import com.example.game.common.dto.ResponseDto;
import com.example.game.config.UserDetailsImpl;
import com.example.game.unit.dto.UnitAttackRequestDto;
import com.example.game.unit.dto.UnitAttackResponseDto;
import com.example.game.unit.dto.UnitMoveRequestDto;
import com.example.game.unit.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
