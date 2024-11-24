package com.example.game.user.controller;

import com.example.game.common.dto.ResponseDto;
import com.example.game.config.UserDetailsImpl;
import com.example.game.user.dto.MyInfoResponseDto;
import com.example.game.user.dto.RequestLogin;
import com.example.game.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign")
    public void signup(@Valid @RequestBody RequestLogin requestLogin) {
        userService.signup(requestLogin);
    }

    @GetMapping("/user/info")
    public ResponseDto<MyInfoResponseDto> myPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(userService.getMyInfo(userDetails.getUser()));
    }
}
