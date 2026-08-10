package com.rose.user.auth.controller;

import com.rose.user.auth.dto.AuthResponse;
import com.rose.user.auth.dto.LoginDto;
import com.rose.user.auth.dto.LoginRequest;
import com.rose.user.auth.dto.LoginResponse;
import com.rose.user.auth.dto.RegisterRequest;
import com.rose.user.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        LoginDto loginDto = authService.login(loginRequest);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        loginDto.refreshToken()
                )
                .body(
                        loginDto.loginResponse()
                );
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@CookieValue("refreshToken") String refreshToken) {
        return authService.refresh(refreshToken);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {

        authService.register(registerRequest);

        //Automatically log in the user after registration
        LoginRequest loginRequest = new LoginRequest(registerRequest.email(), registerRequest.password());
        LoginDto loginDto = authService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(
                        HttpHeaders.SET_COOKIE,
                        loginDto.refreshToken()
                )
                .body(
                        loginDto.loginResponse()
                );
    }
}
