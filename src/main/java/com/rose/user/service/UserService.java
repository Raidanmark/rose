package com.rose.user.service;

import com.rose.common.exception.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.rose.user.dto.CreateUserDto;
import com.rose.user.dto.UserDto;
import com.rose.user.entity.User;
import com.rose.user.mapper.UserMapper;
import com.rose.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto createUser(CreateUserDto createUserDto) {
        String normalizedEmail = createUserDto.email().toLowerCase();
        String normalizedUsername = createUserDto.username().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException(normalizedEmail);
        }

        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new RuntimeException("Username already exists: " + normalizedUsername);
        }

        String passwordHash = passwordEncoder.encode(createUserDto.password());

        User user = User.create(
                createUserDto.username(),
                normalizedEmail,
                passwordHash
        );

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}