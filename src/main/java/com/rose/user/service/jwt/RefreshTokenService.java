package com.rose.user.service.jwt;

import com.rose.user.entity.RefreshToken;
import com.rose.user.entity.User;
import com.rose.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHashService tokenHashService;

    @Value("${jwt.refresh-token-expiration-days}")
    private long refreshTokenExpirationDays;

    public String createRefreshToken(User user) {
        String rawToken = generateSecureToken();
        String tokenHash = tokenHashService.sha256(rawToken);

        RefreshToken refreshToken = RefreshToken.create(
                user,
                tokenHash,
                LocalDateTime.now().plusDays(refreshTokenExpirationDays)
        );

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    public User validateRefreshToken(String rawToken) {
        String tokenHash = tokenHashService.sha256(rawToken);

        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (refreshToken.isExpired() || refreshToken.isRevoked()) {
            throw new IllegalArgumentException("Refresh token has expired");
        }

        return refreshToken.getUser();
    }

    private String generateSecureToken() {
            byte[] bytes = new byte[64];
            new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
