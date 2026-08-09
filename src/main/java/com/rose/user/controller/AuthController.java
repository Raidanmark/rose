package com.rose.user.controller;

import com.rose.user.dto.auth.AuthResponse;
import com.rose.user.dto.auth.AuthTokens;
import com.rose.user.dto.auth.LoginRequest;
import com.rose.user.dto.auth.RegisterRequest;
import com.rose.user.dto.auth.RegisterResponse;
import com.rose.user.dto.user.UserDto;
import com.rose.user.service.AuthService;
import com.rose.user.service.jwt.RefreshTokenCookieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenCookieService refreshTokenCookieService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        AuthTokens authTokens = authService.login(loginRequest);

        ResponseCookie refreshCookie = refreshTokenCookieService.create(authTokens.refreshToken());

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        refreshCookie.toString()
                )
                .body(
                        AuthResponse.bearer(authTokens.accessToken())
                );
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@CookieValue("refreshToken") String refreshToken) {
        return authService.refresh(refreshToken);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {

        UserDto userDto = authService.register(registerRequest);

        //Bad practice:
        AuthTokens authTokens = authService.login(new LoginRequest(registerRequest.email(), registerRequest.password()));

        ResponseCookie refreshCookie = refreshTokenCookieService.create(authTokens.refreshToken());

        RegisterResponse registerResponse = new RegisterResponse(
                AuthResponse.bearer(authTokens.accessToken()),
                userDto
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(
                        HttpHeaders.SET_COOKIE,
                        refreshCookie.toString()
                )
                .body(
                        registerResponse
                );
    }
}
