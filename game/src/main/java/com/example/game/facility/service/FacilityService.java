package com.example.game.facility.service;

import com.example.game.common.dto.ResponseDto;
import com.example.game.common.exception.ErrorCode;
import com.example.game.common.exception.GlobalException;
import com.example.game.facility.dto.FacilityCreateRequestDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            case HEADQUARTERS:
            case INFANTRY_SCHOOL:
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
        // 생성위치에 유닛이 있다면 생산하지 않고 return
        if (unitRepository.findByWorldMap(facility.getWorldMap()).isPresent()) {
            return;
        }
        // 아이템 부족하면 return
        Optional<FacilityItem> facilitySteel = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, ItemType.STEEL);
        Optional<FacilityItem> facilityFood = facilityItemRepository.findWithPessimisticLockByFacilityAndItemType(facility, ItemType.FOOD);

        if(facilitySteel.isEmpty() || facilitySteel.get().getQuantity() < unitType.getSteelCostToCreate()) {
            return;
        }
        if(facilityFood.isEmpty() || facilityFood.get().getQuantity() < unitType.getFoodCostToCreate()) {
            return;
        }

        // 아이템이 충분하면 사용 처리
        facilitySteel.get().useItem(unitType.getSteelCostToCreate());
        facilityFood.get().useItem(unitType.getFoodCostToCreate());

        // 유닛 생성
        unitRepository.save(new Unit(facility.getUser(), facility.getWorldMap(), unitType.getName(), unitType));

        facility.updateProductionTime();
    }
}