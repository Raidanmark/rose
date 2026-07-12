package com.rose.user.dto.user;

import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email
) {
}