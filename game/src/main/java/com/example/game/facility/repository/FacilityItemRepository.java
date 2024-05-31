package com.example.game.facility.repository;

import com.example.game.facility.entity.Facility;
import com.example.game.facility.entity.FacilityItem;
import com.example.game.item.entity.ItemType;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface FacilityItemRepository extends JpaRepository<FacilityItem, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<FacilityItem> findWithPessimisticLockByFacilityAndItemType(Facility facility, ItemType itemType);
}
