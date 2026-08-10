package com.rose.user.auth.dto;

public record LoginDto(
        LoginResponse loginResponse,
        String refreshToken
) {
}
