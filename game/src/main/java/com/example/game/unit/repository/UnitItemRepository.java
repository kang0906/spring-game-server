package com.example.game.unit.repository;

import com.example.game.item.entity.ItemType;
import com.example.game.unit.entity.Unit;
import com.example.game.unit.entity.UnitItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UnitItemRepository extends JpaRepository<UnitItem, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<UnitItem> findWithPessimisticLockByUnitAndItemType(Unit unit, ItemType itemType);
}
