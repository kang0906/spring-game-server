package com.example.game.user.controller;

import com.example.game.config.UserDetailsImpl;
import com.example.game.user.service.KakaoUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class KakaoUserController {

    private final KakaoUserService kakaoUserService;

    @ResponseBody
    @GetMapping("/login-test")
    public String testRequest(@AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("[called GET:/login-test] called by user : {}", userDetails.getUser().getEmail());
        return "user : " + userDetails.getUsername();
    }

    // https://kauth.kakao.com/oauth/authorize?client_id=e0fa0a29b6f980a77e6cad8b0f96639d&redirect_uri=http://localhost:8080/user/kakao/callback&response_type=code
//        카카오로 로그인하기
    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        log.info("[called GET:/user/kakao/callback] param code : {}", code);
        String token = kakaoUserService.kakaoLogin(code);
        // todo : front 서버로 redirect 하도록 수정
        return "redirect:/user/kakao?token="+token;
    }
}
