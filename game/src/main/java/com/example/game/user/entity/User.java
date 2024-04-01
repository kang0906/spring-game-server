package com.example.game.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private String username;
    private String password;
    private LocalDateTime joinDate;
    private LocalDateTime lastAccessDate;



    public User(String email, Long kakaoId, String username, String password) {
        this.email = email;
        this.kakaoId = kakaoId;
        this.username = username;
        this.password = password;
        this.joinDate = LocalDateTime.now();
        this.lastAccessDate = LocalDateTime.now();
    }
}
