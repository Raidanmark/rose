package com.rose.user.dto.user_profile;

import jakarta.validation.constraints.Size;

public record UpdateUserProfileDto(

        @Size(min = 3, max = 64)
        String username,

        @Size(max = 100)
        String bio,

        @Size(max = 500)
        String description,

        @Size(max = 2083)
        String avatarUrl,

        @Size(max = 2083)
        String bannerUrl
) {
}
