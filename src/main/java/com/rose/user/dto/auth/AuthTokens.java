package com.rose.user.dto.auth;

public record AuthTokens (
        String accessToken,
        String refreshToken
){
}
