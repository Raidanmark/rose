package com.rose.user.profile.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

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
