package com.example.game.admin.alert.service;

import com.example.game.admin.alert.entity.AlertLevel;

public interface AlertService {
    String sendAlert(AlertLevel alertLevel, String msg);
}
