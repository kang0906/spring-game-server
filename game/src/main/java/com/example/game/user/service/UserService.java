package com.example.game.user.service;

import com.example.game.common.dto.ResponseDto;
import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.coordinate.entity.Coordinate;
import com.example.game.coordinate.entity.Resources;
import com.example.game.coordinate.repository.coordinate.CoordinateRepository;
import com.example.game.coordinate.service.CoordinateService;
import com.example.game.fleet.service.FleetService;
import com.example.game.user.dto.MyInfoResponseDto;
import com.example.game.user.dto.RequestLogin;
import com.example.game.user.entity.User;
import com.example.game.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CoordinateService coordinateService;
    private final FleetService fleetService;

    public void signup(RequestLogin requestLogin) {
        Optional<User> findUser = userRepository.findByEmail(requestLogin.getEmail());
        if (!findUser.isPresent()) {
            User user = new User(requestLogin.getEmail(), null, requestLogin.getUsername(), bCryptPasswordEncoder.encode(requestLogin.getPassword()));
            userRepository.save(user);
        } else {
            log.warn("존재하는 아이디 입니다.");
            throw new GlobalException(ErrorCode.EXIST_EMAIL);
        }
    }

    public ResponseDto<MyInfoResponseDto> getMyInfo(User user) {
        return ResponseDto.success(new MyInfoResponseDto(user));
    }

    public User setNewUser(User newUser) {
        Coordinate coordinate = coordinateService.makeCoordinateForNewUser();

        fleetService.makeFleetForNewUser(newUser, coordinate);

        return newUser;
    }
}
