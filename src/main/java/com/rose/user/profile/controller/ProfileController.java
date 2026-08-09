package com.rose.user.profile.controller;

import com.rose.user.profile.dto.UpdateProfileRequest;
import com.rose.user.profile.service.ProfileService;
import com.rose.user.profile.dto.PublicProfileResponse;
import com.rose.user.profile.dto.ProfileResponse;
import com.rose.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user-profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService userProfileService;

    @GetMapping("/me")
    public ProfileResponse getMyUserProfile(@AuthenticationPrincipal User user) {
        return userProfileService.getMyUserProfile(user);
    }

    @GetMapping("/{userId}")
    public PublicProfileResponse getUserProfileById(@PathVariable UUID userId) {
        return userProfileService.getUserProfileById(userId);
    }

    @PatchMapping("/me")
    public ProfileResponse updateMyUserProfile(@AuthenticationPrincipal User user,
                                               UpdateProfileRequest updateUserProfileDto) {

        return userProfileService.updateUserProfile(user, updateUserProfileDto);
    }
}
