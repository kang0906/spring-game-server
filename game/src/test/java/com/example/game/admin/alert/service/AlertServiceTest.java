package com.example.game.admin.alert.service;

import com.example.game.admin.alert.entity.AlertLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AlertServiceTest {

    @Autowired
    private AlertService alertService;

    @DisplayName("알림 전송 테스트")
    @Test
    void callWithoutUrlTest() {
        // given

        // when
        alertService.sendAlert(AlertLevel.INFO, "test message");

        // then

    }

}
