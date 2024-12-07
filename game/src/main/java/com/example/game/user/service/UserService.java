package com.example.game.user.service;

import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityItem;
import com.example.game.facility.repository.FacilityItemRepository;
import com.example.game.facility.repository.FacilityRepository;
import com.example.game.item.entity.ItemType;
import com.example.game.system.value.service.GameSystemValueService;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.dto.UserInfoResponseDto;
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

import static com.example.game.common.exception.ErrorCode.DATA_NOT_FOUND;
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
    private final FacilityItemRepository facilityItemRepository;
    private final GameSystemValueService gameSystemValueService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signup(RequestLogin requestLogin) {
        Optional<User> findUser = userRepository.findByEmail(requestLogin.getEmail());
        if (!findUser.isPresent()) {
            String defaultEmblem = gameSystemValueService.getGameSystemValueByProperty("game.user.new.emblem");
            User user = new User(
                    requestLogin.getEmail(),
                    null,
                    "NOOB",
                    bCryptPasswordEncoder.encode(requestLogin.getPassword()),
                    defaultEmblem
            );

            WorldMap spawnPosition = worldMapService.findSpawnPosition(user.getEmail());
            user.setLastLocation(spawnPosition.getAxisX(), spawnPosition.getAxisY());

            userRepository.save(user);
            setNewUser(user, spawnPosition);
        } else {
            log.warn("존재하는 아이디 입니다.");
            throw new GlobalException(ErrorCode.EXIST_EMAIL);
        }
    }

    public UserInfoResponseDto getMyInfo(User user) {
        return new UserInfoResponseDto(user);
    }

    @Transactional
    public void changeUsername(User user, String newUsername) {
        User findUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));
        findUser.changeUsername(newUsername);
    }

    public User setNewUser(User newUser, WorldMap spawnPosition) {
        Facility facility = new Facility(newUser, spawnPosition, HEADQUARTERS.getName(), HEADQUARTERS);
        facilityRepository.save(facility);
        facilityItemRepository.save(new FacilityItem(ItemType.STEEL, 500, facility));
        facilityItemRepository.save(new FacilityItem(ItemType.FOOD, 500, facility));

        Long x = spawnPosition.getAxisX();
        Long y = spawnPosition.getAxisY();

        unitRepository.save(new Unit(newUser, worldMapService.getOrMakeWorldMap(x + 1, y), INFANTRY.getName(), INFANTRY));
        unitRepository.save(new Unit(newUser, worldMapService.getOrMakeWorldMap(x - 1, y), INFANTRY.getName(), INFANTRY));
        unitRepository.save(new Unit(newUser, worldMapService.getOrMakeWorldMap(x, y + 1), CAVALRY.getName(), CAVALRY));
        unitRepository.save(new Unit(newUser, worldMapService.getOrMakeWorldMap(x, y - 1), CAVALRY.getName(), CAVALRY));

        return newUser;
    }

    public UserInfoResponseDto findUserInfo(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));
        return new UserInfoResponseDto(user);
    }
}
