package com.rose.user.service;

import com.rose.user.dto.auth.AuthResponse;
import com.rose.user.dto.auth.LoginRequest;
import com.rose.user.dto.auth.RefreshRequest;
import com.rose.user.dto.auth.RegisterRequest;
import com.rose.user.dto.auth.RegisterResponse;
import com.rose.user.dto.user.UserDto;
import com.rose.user.entity.User;
import com.rose.user.service.jwt.JwtService;
import com.rose.user.service.jwt.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final UserProfileService userProfileService;

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        String normalizedEmail = loginRequest.email()
                .trim()
                .toLowerCase();

        User user = userService.findByEmail(normalizedEmail);

        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.bearer(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest refreshRequest) {
        User user = refreshTokenService.validateRefreshToken(refreshRequest.refreshToken());

       String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());

        return AuthResponse.bearer(newAccessToken, refreshRequest.refreshToken());
    }

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        UserDto userDto = userService.createUser(registerRequest);
        userProfileService.createUserProfile(userDto.id());

        String accessToken = jwtService.generateAccessToken(userDto.id(), userDto.email());
        String refreshToken = refreshTokenService.createRefreshToken(userService.findUserById(userDto.id()));

        return new RegisterResponse(AuthResponse.bearer(accessToken, refreshToken), userDto);
    }
}
