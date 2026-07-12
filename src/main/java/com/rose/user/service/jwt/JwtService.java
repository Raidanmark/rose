package com.rose.user.service.jwt;

import com.rose.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long accessTokenExpirationMillis;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-minutes}") long accessTokenExpirationMinutes
    ) {
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.accessTokenExpirationMillis =
                accessTokenExpirationMinutes * 60_000;
    }

    public String generateAccessToken(UUID userId, String email) {
        Date now = new Date();

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .setIssuedAt(now)
                .expiration(new Date(now.getTime() + accessTokenExpirationMillis))
                .signWith(secretKey)
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        return UUID.fromString(extractClaims(token).getSubject());
    }

    public boolean isTokenValid(String token) {
        return extractClaims(token)
                .getExpiration()
                .after(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID extractUserId(String token) {
        String subject = extractClaims(token).getSubject();
        return UUID.fromString(subject);
    }
}
