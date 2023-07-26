package com.example.game.coordinate.controller;


import com.example.game.config.UserDetailsImpl;
import com.example.game.coordinate.dto.BuildInfraRequestDto;
import com.example.game.coordinate.service.CoordinateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CoordinateController {

    private final CoordinateService coordinateService;

    @PostMapping("/coordinate/infra")
    public boolean buildInfra(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody BuildInfraRequestDto requestDto){

        boolean result = coordinateService.buildInfra(userDetails.getUser(), requestDto);

        return result;
    }
}
