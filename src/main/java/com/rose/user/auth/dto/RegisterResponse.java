package com.rose.user.auth.dto;

import com.rose.user.dto.UserResponse;

public record RegisterResponse(
        AuthResponse authResponse,
        UserResponse userDto
) {
}
