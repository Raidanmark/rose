package com.rose.user.dto;

import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email
) {
}