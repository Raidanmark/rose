package com.rose.user.service;

import com.rose.common.exception.user.EmailAlreadyExistsException;
import com.rose.common.exception.EntityNotFoundException;
import com.rose.common.exception.user.UsernameAlreadyExistsException;
import com.rose.user.dto.user.UpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.rose.user.dto.auth.RegisterRequest;
import com.rose.user.dto.user.UserDto;
import com.rose.user.entity.User;
import com.rose.user.mapper.UserMapper;
import com.rose.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto createUser(RegisterRequest createUserDto) {
        String normalizedEmail = createUserDto.email().toLowerCase();
        String normalizedUsername = createUserDto.username().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException(normalizedEmail);
        }

        validateUsernameIsAvailable(normalizedUsername);

        String passwordHash = passwordEncoder.encode(createUserDto.password());

        User user = User.create(
                normalizedUsername,
                normalizedEmail,
                passwordHash
        );

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    @Transactional
    public UserDto update(UUID id, UpdateDto updateDto) {
        User user = findUserById(id);

        if (updateDto.username() != null && !updateDto.username().isBlank()) {
            String normalizedUsername = updateDto.username().toLowerCase();
            if (!normalizedUsername.equals(user.getUsername()) && userRepository.existsByUsername(normalizedUsername)) {
                throw new UsernameAlreadyExistsException(normalizedUsername);
            }
            user.setUsername(normalizedUsername);
        }

        if (updateDto.email() != null && !updateDto.email().isBlank()) {
            String normalizedEmail = updateDto.email().toLowerCase();
            if (!normalizedEmail.equals(user.getEmail()) && userRepository.existsByEmail(normalizedEmail)) {
                throw new EmailAlreadyExistsException(normalizedEmail);
            }
            user.setEmail(normalizedEmail);
        }

        if (updateDto.password() != null && !updateDto.password().isBlank()) {
            String passwordHash = passwordEncoder.encode(updateDto.password());
            user.setPasswordHash(passwordHash);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    public UserDto getUserById(UUID id) {
        return userMapper.toDto(findUserById(id));
    }

    public User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    public void validateUsernameIsAvailable(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
    }
}