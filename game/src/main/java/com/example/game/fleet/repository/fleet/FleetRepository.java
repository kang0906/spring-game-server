package com.example.game.fleet.repository.fleet;

import com.example.game.coordinate.entity.Coordinate;
import com.example.game.fleet.entity.Fleet;
import com.example.game.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FleetRepository extends JpaRepository<Fleet, Long>, FleetRepositoryCustom {
    public Fleet findByCoordinate(Coordinate coordinate);
    public List<Fleet> findByUser(User user);
}
