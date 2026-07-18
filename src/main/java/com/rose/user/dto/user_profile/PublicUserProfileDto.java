package com.rose.user.dto.user_profile;

public record PublicUserProfileDto(
        String username,
        String bio,
        String description,
        String avatarUrl,
        String bannerUrl
) {
}
