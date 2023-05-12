package com.example.game.common.dto;

import com.example.game.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Getter
public class SearchDto {
    private User user;
    private String search;
    private LocalDate startDate;
    private LocalDate endDate;
    private Pageable pageable;

    @Builder
    public SearchDto(User user, String search, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        this.user = user;
        this.search = search;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pageable = pageable;
    }
}
