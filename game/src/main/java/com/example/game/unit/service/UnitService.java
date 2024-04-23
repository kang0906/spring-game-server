package com.example.game.unit.service;

import com.example.game.common.exception.GlobalException;
import com.example.game.unit.dto.UnitAttackRequestDto;
import com.example.game.unit.dto.UnitMoveRequestDto;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.entity.User;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.repository.WorldMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.game.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitService {

    private final UnitRepository unitRepository;
    private final WorldMapRepository worldMapRepository;

    @Transactional
    public boolean unitMove(UnitMoveRequestDto requestDto, User requestUser) {

        // 이동 값이 (0,0) 일 경우 이동로직을 수행하지 않고 return
        if (requestDto.getX() == 0 && requestDto.getY() == 0) {
            return true;
        }

        // 이동가능한 위치인지(이동 거리) 확인
        if (requestDto.getY() > 1 || requestDto.getX() > 1
                || requestDto.getY() < -1 || requestDto.getX() < -1) {
            throw new GlobalException(VALIDATION_FAIL);
        }

        // 유닛이 요청한 유저의 소유인지 확인
        Unit unit = checkUnitOwner(requestDto.getUnitId(), requestUser);

        // todo : 이동하려는 좌표에 락 설정

        // 이동하려는 곳이 비어있는지 확인
        WorldMap unitPos = unit.getWorldMap();
        WorldMap destination = worldMapRepository
                .findByAxisXAndAxisY(unitPos.getAxisX() + requestDto.getX(), unitPos.getAxisY() + requestDto.getY());
        if (destination == null) {
            destination = worldMapRepository.save(
                    new WorldMap("", unitPos.getAxisX() + requestDto.getX(), unitPos.getAxisY() + requestDto.getY()));
        } else if (unitRepository.findByWorldMap(destination).isPresent()) {
            throw new GlobalException(DESTINATION_NOT_EMPTY);
        }

        // 이동 수행
        unit.move(destination);

        return true;
    }

    @Transactional
    public void unitAttack(UnitAttackRequestDto requestDto, User requestUser) {

        Unit unit = checkUnitOwner(requestDto.getUnitId(), requestUser);

        // 공격 범위 확인

        // 공격 수행

    }

    private Unit checkUnitOwner(Long unitId, User requestUser) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));
        if (!unit.getUser().getUserId().equals(requestUser.getUserId())) {
            throw new GlobalException(CANT_EDIT);
        }
        return unit;
    }
}
