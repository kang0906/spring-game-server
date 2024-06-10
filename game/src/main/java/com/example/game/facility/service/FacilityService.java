package com.example.game.facility.service;

import com.example.game.common.dto.ResponseDto;
import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.config.UserDetailsImpl;
import com.example.game.facility.dto.FacilityCreateRequestDto;
import com.example.game.facility.dto.FacilityItemMoveRequestDto;
import com.example.game.facility.dto.FacilityResponseDto;
import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityItem;
import com.example.game.facility.entity.FacilityType;
import com.example.game.facility.repository.FacilityItemRepository;
import com.example.game.facility.repository.FacilityRepository;
import com.example.game.item.entity.ItemType;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitItem;
import com.example.game.unit.entity.UnitType;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.game.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityItemRepository facilityItemRepository;
    private final UnitRepository unitRepository;
    private final WorldMapRepository worldMapRepository;
    private final UnitItemRepository unitItemRepository;

    @Transactional
    public ResponseDto<String> facilityItemMove(User user, FacilityItemMoveRequestDto requestDto) {

        if (requestDto.getQuantity() < 0) {
           throw new GlobalException(CAN_NOT_USE_NEGATIVE_NUMBER);
        }

        Facility facility = facilityRepository.findById(requestDto.getFacilityId())
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));

        FacilityItem facilityItem = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, requestDto.getItemType())
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));

        if (facilityItem.getQuantity() < requestDto.getQuantity()) {
            throw new GlobalException(NOT_ENOUGH_ITEM);
        }

        Unit unit = unitRepository.findByWorldMap(facility.getWorldMap())
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));

        UnitItem unitItem = unitItemRepository.findWithPessimisticLockByUnitAndItemType(unit, requestDto.getItemType()).orElse(null);

        if (unitItem == null) {
            unitItem = unitItemRepository.save(new UnitItem(unit, requestDto.getItemType(), 0));
        }

        facilityItem.useItem(requestDto.getQuantity());
        unitItem.addItem(requestDto.getQuantity());

        return ResponseDto.success("success");
    }

    @Transactional
    public ResponseDto<FacilityResponseDto> facilityCreate(User user, FacilityCreateRequestDto requestDto) {

        Unit unit = unitRepository.findById(requestDto.getUnitId())
                .orElseThrow(() -> new GlobalException(DATA_NOT_FOUND));
        if (!unit.getUser().getUserId().equals(user.getUserId())) {
            throw new GlobalException(CANT_EDIT);
        }

        WorldMap worldMap = worldMapRepository.findByAxisXAndAxisY(unit.getAxisX(), unit.getAxisY());
        if (facilityRepository.findByWorldMap(worldMap).isPresent()) {
            throw new GlobalException(ErrorCode.DESTINATION_NOT_EMPTY);
        }

        FacilityType facilityType = FacilityType.valueOf(requestDto.getFacilityType());

        Optional<UnitItem> unitItem = unitItemRepository.findWithPessimisticLockByUnitAndItemType(unit, ItemType.STEEL);
        if (unitItem.isEmpty() || unitItem.get().getQuantity() < facilityType.getSteelCostToCreate()) {
            throw new GlobalException(ErrorCode.NOT_ENOUGH_ITEM);
        }
        unitItem.get().useItem(facilityType.getSteelCostToCreate());

        Facility save = facilityRepository.save(new Facility(user, worldMap, requestDto.getFacilityType(), facilityType));

        return ResponseDto.success(new FacilityResponseDto(save));
    }

    @Transactional
    public void facilityStatusUpdate(Facility facility) {
        LocalDateTime productionStartTime = facility.getProductionTime();
        if (productionStartTime.plusHours(1).isAfter(LocalDateTime.now())) {
            return ;
        }

        switch (facility.getType()) {
            case FARM:
                itemProductionInFacility(facility, ItemType.FOOD);
                break;
            case STEEL_FACTORY:
                itemProductionInFacility(facility, ItemType.STEEL);
                break;
            case INFANTRY_SCHOOL, HEADQUARTERS:
                unitProductionInFacility(facility, UnitType.INFANTRY);
                break;
            case ARTILLERY_SCHOOL:
                unitProductionInFacility(facility, UnitType.ARTILLERY);
                break;
            case CAVALRY_SCHOOL:
                unitProductionInFacility(facility, UnitType.CAVALRY);
                break;
            default:
                throw new GlobalException(INTERNAL_SERVER_ERROR);
        }
    }

    private void itemProductionInFacility(Facility facility, ItemType itemType) {
        Optional<FacilityItem> facilityFoodOptional = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, itemType);
        FacilityItem facilityFood;
        if (facilityFoodOptional.isEmpty()) {
            facilityFood = facilityItemRepository.save(new FacilityItem(itemType, 0, facility));
        } else {
            facilityFood = facilityFoodOptional.get();
        }
        facilityFood.addItem(100);

        facility.updateProductionTime();
    }

    private void unitProductionInFacility(Facility facility, UnitType unitType) {
        if (unitRepository.findByWorldMap(facility.getWorldMap()).isPresent()) {
            return;
        }
        Optional<FacilityItem> facilitySteel = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, ItemType.STEEL);
        Optional<FacilityItem> facilityFood = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, ItemType.FOOD);

        if(facilitySteel.isEmpty() || facilitySteel.get().getQuantity() < unitType.getSteelCostToCreate()) {
            return;
        }
        if(facilityFood.isEmpty() || facilityFood.get().getQuantity() < unitType.getFoodCostToCreate()) {
            return;
        }

        facilitySteel.get().useItem(unitType.getSteelCostToCreate());
        facilityFood.get().useItem(unitType.getFoodCostToCreate());

        unitRepository.save(new Unit(facility.getUser(), facility.getWorldMap(), unitType.getName(), unitType));

        facility.updateProductionTime();
    }
}
