package com.rose.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(

        @NotBlank
        String username,

        @Email
        @NotBlank
        @Size(max = 254)
        String email,

        @NotBlank
        @Size(min = 8, max = 128)
        String password
) {
}