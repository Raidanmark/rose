package com.rose.user.profile.dto;

import com.rose.user.profile.entity.MediaLink;

import java.util.List;
import java.util.UUID;

public record ProfileResponse(

        UUID id,

        String username,

        String bio,

        String description,

        String avatarUrl,

        String bannerUrl,

        List<MediaLink> mediaLinks
) {
}
