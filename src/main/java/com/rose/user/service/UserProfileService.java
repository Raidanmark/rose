package com.rose.user.service;

import com.rose.common.exception.EntityNotFoundException;
import com.rose.user.dto.user_profile.PublicUserProfileDto;
import com.rose.user.dto.user_profile.UpdateUserProfileDto;
import com.rose.user.dto.user_profile.UserProfileDto;
import com.rose.user.entity.User;
import com.rose.user.entity.UserProfile;
import com.rose.user.mapper.UserProfileMapper;
import com.rose.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final UserService userService;

    public void createUserProfile(UUID userId) {
        User user = userService.findUserById(userId);
        UserProfile userProfile = UserProfile.create(user);
        userProfileRepository.save(userProfile);
    }

    public UserProfileDto getMyUserProfile(User user) {
        UserProfile profile = findUserProfileById(user.getId());
        return userProfileMapper.toDto(profile);
    }

    public PublicUserProfileDto getUserProfileById(UUID userId) {
        UserProfile profile = findUserProfileById(userId);
        return userProfileMapper.toPublicDto(profile);
    }

    @Transactional
    public UserProfileDto updateUserProfile(User user, UpdateUserProfileDto updateUserProfileDto) {
        UserProfile profile = findUserProfileById(user.getId());

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

    private UserProfile findUserProfileById(UUID userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found for user: " + userId));
    }
}
