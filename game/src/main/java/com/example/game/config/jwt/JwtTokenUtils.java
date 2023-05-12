package com.example.game.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.game.config.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.game.config.jwt.JwtProperties.*;


@Component
public class JwtTokenUtils {

    @Value("${jwt.secretKey}")
    private String JWT_SECRET;

    public String generateJwtToken(UserDetailsImpl userDetailsImpl) {
        return JWT.create()
                .withSubject(SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim(CLAIM_USER_EMAIL, userDetailsImpl.getUser().getEmail())
                .sign(Algorithm.HMAC512(JWT_SECRET));
    }


    public String extractUserEmail(String jwtToken) {

        return JWT.require(Algorithm.HMAC512(JWT_SECRET)).build()
                .verify(jwtToken)
                .getClaim(CLAIM_USER_EMAIL).asString();
    }
}
