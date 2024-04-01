package com.example.game.unit.controller;

import com.example.game.common.dto.ResponseDto;
import com.example.game.config.UserDetailsImpl;
import com.example.game.unit.dto.UnitMoveRequestDto;
import com.example.game.unit.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UnitController {

    private final UnitService unitService;

    @ResponseBody
    @PostMapping("/unit/move")
    public ResponseDto<String> unitMove(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UnitMoveRequestDto requestDto) {

        log.info("requestDto = {}", requestDto);
        unitService.unitMove(requestDto, userDetails.getUser());

        return ResponseDto.success("success");
    }
}