package com.rose.user.service.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RefreshTokenCookieService {

    @Value("${jwt.refresh-token-expiration-days}")
    private long refreshTokenCookieDuration;

    public ResponseCookie create(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(Duration.ofDays(refreshTokenCookieDuration))
                .build();
    }
}
