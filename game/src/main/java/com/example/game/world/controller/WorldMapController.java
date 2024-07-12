package com.example.game.world.controller;

import com.example.game.common.dto.ResponseDto;
import com.example.game.config.UserDetailsImpl;
import com.example.game.world.dto.WorldMapInfoDto;
import com.example.game.world.dto.WorldMapLoadRequestDto;
import com.example.game.world.dto.WorldMapLoadResponseDto;
import com.example.game.world.service.WorldMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WorldMapController {

    private final WorldMapService worldMapService;

    @ResponseBody
    @PostMapping("/map/load")
    public ResponseDto<WorldMapLoadResponseDto> loadWorldMap(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody WorldMapLoadRequestDto requestDto) {

        return ResponseDto.success(worldMapService.loadWorldMap(userDetails.getUser(), requestDto));
    }

    @ResponseBody
    @GetMapping("/map/info")
    public ResponseDto<WorldMapInfoDto> getWorldMapInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseDto.success(worldMapService.getWorldMapInfo());
    }
}
