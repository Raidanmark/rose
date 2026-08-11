package com.rose.common.config.auth.security;

import com.rose.user.entity.User;
import com.rose.user.repository.UserRepository;
import com.rose.user.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            boolean valid = jwtService.isTokenValid(token);

            log.info("JWT valid: {}", valid);

            if (valid) {

                UUID userId = jwtService.extractUserId(token);

                log.info("JWT userId: {}", userId);

                User user = userRepository.findById(userId)
                        .orElse(null);

                log.info("User found: {}", user != null);

                if (user != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    log.info("Authentication successfully set for user {}", userId);
                }
            }

        } catch (Exception e) {
            log.error("JWT AUTHENTICATION ERROR", e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/auth/register")
                || path.equals("/auth/login")
                || path.equals("/auth/refresh")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs/");
    }
}
