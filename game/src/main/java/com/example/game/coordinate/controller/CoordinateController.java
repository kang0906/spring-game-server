package com.example.game.coordinate.controller;


import com.example.game.config.UserDetailsImpl;
import com.example.game.coordinate.service.CoordinateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CoordinateController {

    private final CoordinateService coordinateService;

    @PostMapping("/coordinate/infra")
    public boolean buildInfra(@AuthenticationPrincipal UserDetailsImpl userDetails){

        boolean result = coordinateService.buildInfra(userDetails.getUser());

        return result;
    }
}
