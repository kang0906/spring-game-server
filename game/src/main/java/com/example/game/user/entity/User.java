package com.example.game.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String kakaoId;
    private String username;
    private String password;

    public User(String email, String kakaoId, String username, String password) {
        this.email = email;
        this.kakaoId = kakaoId;
        this.username = username;
        this.password = password;
    }
}
