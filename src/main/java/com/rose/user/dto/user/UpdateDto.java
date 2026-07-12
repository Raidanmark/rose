package com.rose.user.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateDto (

        @Size(min = 3, max = 64)
        String username,

        @Email
        @Size(max = 254)
        String email,

        @Size(min = 8, max = 128)
        String password
) {
}
