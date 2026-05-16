package com.sarjeev.booktheshow.repositories;

import com.sarjeev.booktheshow.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("update RefreshToken refreshToken set refreshToken.revoked = true where refreshToken.user.id = :userId and refreshToken.revoked = false")
    int revokeAllActiveTokensForUser(@Param("userId") UUID userId);

    @Modifying
    @Query("delete from RefreshToken refreshToken where refreshToken.expiresAt < :now or refreshToken.revoked = true")
    int deleteExpiredOrRevokedTokens(@Param("now") Instant now);
}
