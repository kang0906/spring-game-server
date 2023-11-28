package com.example.game.coordinate.dto;

import com.example.game.coordinate.entity.InfraList;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InfraData {
    private final String name;
    private final int titaniumCost;
    private final int gasCost;

    public InfraData(InfraList infraList) {
        this.name = infraList.getName();
        this.titaniumCost = infraList.getTitaniumCost();
        this.gasCost = infraList.getGasCost();
    }
}
