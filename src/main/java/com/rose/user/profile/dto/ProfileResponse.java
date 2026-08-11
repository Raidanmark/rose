package com.rose.user.profile.dto;

import java.util.UUID;

public record ProfileResponse(

        UUID id,

        String username,

        String bio,

        String description,

        String avatarUrl,

        String bannerUrl
) {
}
