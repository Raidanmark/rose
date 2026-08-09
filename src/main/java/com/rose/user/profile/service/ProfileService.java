package com.rose.user.profile.service;

import com.rose.common.exception.EntityNotFoundException;
import com.rose.user.profile.dto.ProfileResponse;
import com.rose.user.profile.dto.PublicProfileResponse;
import com.rose.user.profile.dto.UpdateProfileRequest;
import com.rose.user.entity.User;
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

    private final ProfileRepository userProfileRepository;
    private final ProfileMapper userProfileMapper;
    private final UserService userService;

    public void createUserProfile(UUID userId) {
        User user = userService.findUserById(userId);
        Profile userProfile = Profile.create(user);
        userProfileRepository.save(userProfile);
    }

    public ProfileResponse getMyUserProfile(User user) {
        Profile profile = findUserProfileById(user.getId());
        return userProfileMapper.toDto(profile);
    }

    public PublicProfileResponse getUserProfileById(UUID userId) {
        Profile profile = findUserProfileById(userId);
        return userProfileMapper.toPublicDto(profile);
    }

    @Transactional
    public ProfileResponse updateUserProfile(User user, UpdateProfileRequest updateUserProfileDto) {
        Profile profile = findUserProfileById(user.getId());

        updateUsername(profile.getUser(), updateUserProfileDto.username());

        userProfileMapper.updateEntity(updateUserProfileDto, profile);
        userProfileRepository.save(profile);

        return userProfileMapper.toDto(profile);
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
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found for user: " + userId));
    }
}
