package com.rose.user.auth.service;

import com.rose.user.auth.dto.AuthResponse;
import com.rose.user.auth.dto.AuthTokens;
import com.rose.user.auth.dto.LoginRequest;
import com.rose.user.auth.dto.RegisterRequest;
import com.rose.user.dto.UserResponse;
import com.rose.user.entity.User;
import com.rose.user.auth.service.jwt.JwtService;
import com.rose.user.auth.service.jwt.RefreshTokenService;
import com.rose.user.profile.service.ProfileService;
import com.rose.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final ProfileService userProfileService;

    @Transactional
    public AuthTokens login(LoginRequest loginRequest) {
        String normalizedEmail = loginRequest.email()
                .trim()
                .toLowerCase();

        User user = userService.findByEmail(normalizedEmail);

        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthTokens(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refresh(String refreshToken) {
        User user = refreshTokenService.validateRefreshToken(refreshToken);

       String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());

        return AuthResponse.bearer(newAccessToken);
    }

    @Transactional
    public UserResponse register(RegisterRequest registerRequest) {
        UserResponse userDto = userService.createUser(registerRequest);
        userProfileService.createUserProfile(userDto.id());

        return userDto;
    }
}
