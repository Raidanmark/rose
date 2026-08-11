package com.rose.user.profile.dto;

import com.rose.user.profile.entity.MediaLink;
import jakarta.validation.constraints.Size;

import java.util.List;

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
        String bannerUrl,

        @Size(max = 16)
        List<MediaLink> mediaLinks
) {
}
