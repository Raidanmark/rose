package com.rose.user.dto.auth;

import com.rose.user.dto.user.UserDto;

public record RegisterResponse(
        AuthResponse authResponse,
        UserDto userDto
) {
}
