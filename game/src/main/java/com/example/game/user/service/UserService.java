package com.example.game.user.service;

import com.example.game.common.dto.ResponseDto;
import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityType;
import com.example.game.facility.repository.FacilityRepository;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitType;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.dto.MyInfoResponseDto;
import com.example.game.user.dto.RequestLogin;
import com.example.game.user.entity.User;
import com.example.game.user.repository.UserRepository;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.service.WorldMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.game.facility.entity.FacilityType.HEADQUARTERS;
import static com.example.game.unit.entity.UnitType.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final WorldMapService worldMapService;
    private final FacilityRepository facilityRepository;
    private final UnitRepository unitRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signup(RequestLogin requestLogin) {
        Optional<User> findUser = userRepository.findByEmail(requestLogin.getEmail());
        if (!findUser.isPresent()) {
            User user = new User(requestLogin.getEmail(), null, requestLogin.getUsername(), bCryptPasswordEncoder.encode(requestLogin.getPassword()));

            WorldMap spawnPosition = worldMapService.findSpawnPosition(user.getEmail());
            user.setLastLocation(spawnPosition.getAxisX(), spawnPosition.getAxisY());

            userRepository.save(user);
            setNewUser(user, spawnPosition);
        } else {
            log.warn("존재하는 아이디 입니다.");
            throw new GlobalException(ErrorCode.EXIST_EMAIL);
        }
    }

    public ResponseDto<MyInfoResponseDto> getMyInfo(User user) {
        return ResponseDto.success(new MyInfoResponseDto(user));
    }

    public User setNewUser(User newUser, WorldMap spawnPosition) {
        Facility facility = new Facility(newUser, spawnPosition, HEADQUARTERS.getName(), HEADQUARTERS);
        facilityRepository.save(facility);

        Long x = spawnPosition.getAxisX();
        Long y = spawnPosition.getAxisY();

        unitRepository.save(new Unit(newUser, worldMapService.getOrMakeWorldMap(x + 1, y), INFANTRY.getName(), INFANTRY));
        unitRepository.save(new Unit(newUser, worldMapService.getOrMakeWorldMap(x - 1, y), INFANTRY.getName(), INFANTRY));
        unitRepository.save(new Unit(newUser, worldMapService.getOrMakeWorldMap(x, y + 1), CAVALRY.getName(), CAVALRY));
        unitRepository.save(new Unit(newUser, worldMapService.getOrMakeWorldMap(x, y - 1), CAVALRY.getName(), CAVALRY));

        return newUser;
    }
}
