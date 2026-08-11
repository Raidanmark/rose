package com.rose.user.profile.dto;

public record PublicProfileResponse(
        String username,
        String bio,
        String description,
        String avatarUrl,
        String bannerUrl
) {
}
