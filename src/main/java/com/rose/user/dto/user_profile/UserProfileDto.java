package com.rose.user.dto.user_profile;

import java.util.UUID;

public record UserProfileDto (

        UUID id,

        String username,

        String bio,

        String description,

        String avatarUrl,

        String bannerUrl
) {
}
