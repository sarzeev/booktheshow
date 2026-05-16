package com.sarjeev.booktheshow.services;

import com.sarjeev.booktheshow.entities.RefreshToken;
import com.sarjeev.booktheshow.entities.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken rotateRefreshToken(String token);

    void revokeRefreshToken(String token);

    void revokeAllForUser(User user);
}
