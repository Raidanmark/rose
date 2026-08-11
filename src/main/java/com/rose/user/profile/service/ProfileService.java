package com.rose.user.profile.service;

import com.rose.common.exception.EntityNotFoundException;
import com.rose.user.entity.User;
import com.rose.user.profile.dto.ProfileResponse;
import com.rose.user.profile.dto.PublicProfileResponse;
import com.rose.user.profile.dto.UpdateProfileRequest;
import com.rose.user.profile.entity.Profile;
import com.rose.user.profile.mapper.ProfileMapper;
import com.rose.user.profile.repository.ProfileRepository;
import com.rose.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final UserService userService;

    public void createUserProfile(UUID userId) {
        User user = userService.findUserById(userId);
        Profile userProfile = Profile.create(user);
        profileRepository.save(userProfile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getMyUserProfile(User user) {
        Profile profile = findUserProfileById(user.getId());
        return profileMapper.toDto(profile);
    }

    @Transactional(readOnly = true)
    public PublicProfileResponse getUserProfileById(UUID userId) {
        Profile profile = findUserProfileById(userId);
        return profileMapper.toPublicDto(profile);
    }

    @Transactional
    public ProfileResponse updateUserProfile(User user, UpdateProfileRequest updateUserProfileDto) {
        Profile profile = findUserProfileById(user.getId());

        updateUsername(profile.getUser(), updateUserProfileDto.username());

        profileMapper.updateEntity(updateUserProfileDto, profile);
        profileRepository.save(profile);

        return profileMapper.toDto(profile);
    }

    private void updateUsername(User user, String newUsername) {
        if (newUsername == null || newUsername.equals(user.getUsername())) {
            return;
        }

        String normalizedUsername = newUsername.trim().toLowerCase();
        userService.validateUsernameIsAvailable(normalizedUsername);

        user.setUsername(newUsername);
    }

    private Profile findUserProfileById(UUID userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found for user: " + userId));
    }
}
