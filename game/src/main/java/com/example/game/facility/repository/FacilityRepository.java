package com.example.game.facility.repository;

import com.example.game.facility.entity.Facility;
import com.example.game.unit.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long>, FacilityRepositoryCustom {


}
