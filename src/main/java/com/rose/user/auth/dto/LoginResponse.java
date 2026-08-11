package com.rose.user.auth.dto;

import com.rose.user.dto.UserResponse;

public record LoginResponse(
        AuthResponse authResponse,
        UserResponse userResponse
) {
}
