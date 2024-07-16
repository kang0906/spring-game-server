package com.example.game.admin.alert.entity;

public enum AlertLevel {
    INFO("[INFO]"),
    WARN("[WARN]"),
    ERROR("[ERROR]");

    private String display;

    AlertLevel(String display) {
        this.display = display;
    }
}
