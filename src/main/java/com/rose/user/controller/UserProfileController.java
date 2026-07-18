package com.rose.user.controller;

import com.rose.user.dto.user_profile.PublicUserProfileDto;
import com.rose.user.dto.user_profile.UpdateUserProfileDto;
import com.rose.user.dto.user_profile.UserProfileDto;
import com.rose.user.entity.User;
import com.rose.user.service.UserProfileService;
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
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public UserProfileDto getMyUserProfile(@AuthenticationPrincipal User user) {
        return userProfileService.getMyUserProfile(user);
    }

    @GetMapping("/{userId}")
    public PublicUserProfileDto getUserProfileById(@PathVariable UUID userId) {
        return userProfileService.getUserProfileById(userId);
    }

    @PatchMapping("/me")
    public UserProfileDto updateMyUserProfile(@AuthenticationPrincipal User user,
                                              UpdateUserProfileDto updateUserProfileDto) {

        return userProfileService.updateUserProfile(user, updateUserProfileDto);
    }
}
