package com.example.game.coordinate.repository.infra;

import com.example.game.coordinate.entity.Infra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfraRepository extends JpaRepository<Infra, Long>, InfraRepositoryCustom {

}
