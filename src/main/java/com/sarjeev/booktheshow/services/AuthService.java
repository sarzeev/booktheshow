package com.sarjeev.booktheshow.services;

import com.sarjeev.booktheshow.requests.LoginRequest;
import com.sarjeev.booktheshow.requests.LogoutRequest;
import com.sarjeev.booktheshow.requests.RefreshTokenRequest;
import com.sarjeev.booktheshow.requests.RegisterRequest;
import com.sarjeev.booktheshow.responses.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    void logout(LogoutRequest request);
}
