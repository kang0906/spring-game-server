package com.example.game.unit.service;

import com.example.game.common.dto.ResponseDto;
import com.example.game.common.exception.GlobalException;
import com.example.game.config.UserDetailsImpl;
import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityItem;
import com.example.game.facility.repository.FacilityItemRepository;
import com.example.game.facility.repository.FacilityRepository;
import com.example.game.item.entity.ItemType;
import com.example.game.unit.dto.*;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitItem;
import com.example.game.unit.repository.UnitItemRepository;
import com.example.game.unit.repository.UnitRepository;
import com.example.game.user.entity.User;
import com.example.game.world.entity.WorldMap;
import com.example.game.world.repository.WorldMapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import static com.example.game.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UnitService {

    private final UnitRepository unitRepository;
    private final WorldMapRepository worldMapRepository;
    private final UnitItemRepository unitItemRepository;
    private final FacilityRepository facilityRepository;
    private final FacilityItemRepository facilityItemRepository;


    @Transactional
    public ResponseDto<String> unitItemMove(User user, UnitItemMoveRequestDto requestDto) {

        if (requestDto.getQuantity() < 0) {
            throw new GlobalException(CAN_NOT_USE_NEGATIVE_NUMBER);
        }

        Unit unit = unitRepository.findById(requestDto.getUnitId())
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));

        UnitItem unitItem = unitItemRepository.findWithPessimisticLockByUnitAndItemType(unit, requestDto.getItemType())
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));


        if (unitItem.getQuantity() < requestDto.getQuantity()) {
            throw new GlobalException(NOT_ENOUGH_ITEM);
        }

        Facility facility = facilityRepository.findByWorldMap(unit.getWorldMap())
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));

        FacilityItem facilityItem = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, requestDto.getItemType()).orElse(null);

        if (facilityItem == null) {
            facilityItem = facilityItemRepository.save(new FacilityItem(requestDto.getItemType(), 0, facility));
        }

        unitItem.useItem(requestDto.getQuantity());
        facilityItem.addItem(requestDto.getQuantity());

        return ResponseDto.success("success");
    }

    @Transactional
    public void unitMove(UnitMoveRequestDto requestDto, User requestUser) {

        // 이동 값이 (0,0) 일 경우 이동로직을 수행하지 않고 return
        if (requestDto.getX() == 0 && requestDto.getY() == 0) {
            return ;
        }

        // 이동가능한 위치인지(이동 거리) 확인

        int x = (int)(long)requestDto.getX();
        int y = (int)(long)requestDto.getY();

        x = Math.abs(x);
        y = Math.abs(y);

        log.info("checkUnitRange : ({}, {})", x, y);

        if (1 < x + y) {
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
    }

    @Transactional
    public UnitAttackResponseDto unitAttack(UnitAttackRequestDto requestDto, User requestUser) {

        Unit unit = checkUnitOwner(requestDto.getUnitId(), requestUser);

        Unit targetUnit = unitRepository.findById(requestDto.getTargetId())
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));

        // 공격 범위 확인 (UnitType Enum 으로 범위 확인)
        checkUnitAttackRange(unit, targetUnit);

        checkFriendlyFire(unit, targetUnit);

        // 공격 수행
        targetUnit.takeAttackFrom(unit);

        if(targetUnit.getHp() <= 0) {
            log.info("unit {} deleted(owner {})", targetUnit.getUnitId(), targetUnit.getUser().getUserId());
            unitRepository.delete(targetUnit);
        }

        return new UnitAttackResponseDto(new UnitResponseDto(unit), new UnitResponseDto(targetUnit));
    }

    private void checkFriendlyFire(Unit unit, Unit targetUnit) {
        if (unit.getUser().getUserId() == targetUnit.getUser().getUserId()) {
            throw new GlobalException(FRIENDLY_FIRE_NOT_ALLOWED);
        }
    }

    private Unit checkUnitOwner(Long unitId, User requestUser) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));
        if (!unit.getUser().getUserId().equals(requestUser.getUserId())) {
            throw new GlobalException(CANT_EDIT);
        }
        return unit;
    }

    private void checkUnitAttackRange(Unit unit, Unit targetUnit) {
        int range = unit.getType().getRange();

        int x = (int) (Math.abs(unit.getAxisX()) - Math.abs(targetUnit.getAxisX()));
        int y = (int) (Math.abs(unit.getAxisY()) - Math.abs(targetUnit.getAxisY()));

        x = Math.abs(x);
        y = Math.abs(y);

        if (range < x + y) {
            throw new GlobalException(OUT_OF_RANGE);
        }
    }
}
