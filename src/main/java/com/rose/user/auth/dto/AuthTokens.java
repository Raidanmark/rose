package com.rose.user.auth.dto;

public record AuthTokens (
        String accessToken,
        String refreshToken
){
}
