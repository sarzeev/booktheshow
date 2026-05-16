package com.sarjeev.booktheshow.services.impl;

import com.sarjeev.booktheshow.entities.RefreshToken;
import com.sarjeev.booktheshow.entities.User;
import com.sarjeev.booktheshow.exceptions.InvalidTokenException;
import com.sarjeev.booktheshow.repositories.RefreshTokenRepository;
import com.sarjeev.booktheshow.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${booktheshow.security.jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .user(user)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public RefreshToken rotateRefreshToken(String token) {
        RefreshToken existingToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh token is invalid"));
        if (!existingToken.isUsable()) {
            throw new InvalidTokenException("Refresh token is expired or revoked");
        }
        existingToken.setRevoked(true);
        refreshTokenRepository.save(existingToken);
        return createRefreshToken(existingToken.getUser());
    }

    @Override
    @Transactional
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh token is invalid"));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public void revokeAllForUser(User user) {
        refreshTokenRepository.revokeAllActiveTokensForUser(user.getId());
    }
}
