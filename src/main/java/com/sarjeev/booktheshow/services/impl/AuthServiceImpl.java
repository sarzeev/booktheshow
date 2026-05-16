package com.sarjeev.booktheshow.services.impl;

import com.sarjeev.booktheshow.entities.RefreshToken;
import com.sarjeev.booktheshow.entities.Role;
import com.sarjeev.booktheshow.entities.User;
import com.sarjeev.booktheshow.exceptions.AccessDeniedException;
import com.sarjeev.booktheshow.exceptions.DuplicateResourceException;
import com.sarjeev.booktheshow.exceptions.ResourceNotFoundException;
import com.sarjeev.booktheshow.mappers.UserMapper;
import com.sarjeev.booktheshow.repositories.RoleRepository;
import com.sarjeev.booktheshow.repositories.UserRepository;
import com.sarjeev.booktheshow.requests.LoginRequest;
import com.sarjeev.booktheshow.requests.LogoutRequest;
import com.sarjeev.booktheshow.requests.RefreshTokenRequest;
import com.sarjeev.booktheshow.requests.RegisterRequest;
import com.sarjeev.booktheshow.responses.AuthResponse;
import com.sarjeev.booktheshow.security.JwtService;
import com.sarjeev.booktheshow.services.AuthService;
import com.sarjeev.booktheshow.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE = "ROLE_ATTENDEE";
    private static final Set<String> SELF_ASSIGNABLE_ROLES = Set.of("ROLE_ATTENDEE", "ROLE_ORGANIZER", "ROLE_STAFF");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new DuplicateResourceException("A user with this email already exists");
        }

        Set<Role> roles = resolveRequestedRoles(request.roles());
        User user = User.builder()
                .firstName(request.firstName().trim())
                .lastName(request.lastName().trim())
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .roles(roles)
                .build();

        User savedUser = userRepository.save(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);
        String accessToken = jwtService.generateAccessToken(savedUser);
        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                jwtService.getAccessTokenExpirationMs(),
                userMapper.toResponse(savedUser)
        );
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.password())
        );
        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        refreshTokenService.revokeAllForUser(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        String accessToken = jwtService.generateAccessToken(user);
        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                jwtService.getAccessTokenExpirationMs(),
                userMapper.toResponse(user)
        );
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(request.refreshToken());
        User user = newRefreshToken.getUser();
        String accessToken = jwtService.generateAccessToken(user);
        return new AuthResponse(
                accessToken,
                newRefreshToken.getToken(),
                "Bearer",
                jwtService.getAccessTokenExpirationMs(),
                userMapper.toResponse(user)
        );
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        refreshTokenService.revokeRefreshToken(request.refreshToken());
    }

    private Set<Role> resolveRequestedRoles(Set<String> requestedRoles) {
        Set<String> roleNames = requestedRoles == null || requestedRoles.isEmpty()
                ? Set.of(DEFAULT_ROLE)
                : requestedRoles.stream()
                .map(this::normalizeRoleName)
                .collect(Collectors.toUnmodifiableSet());

        if (!SELF_ASSIGNABLE_ROLES.containsAll(roleNames)) {
            throw new AccessDeniedException("One or more requested roles cannot be self-assigned");
        }

        return roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
    }

    private String normalizeRoleName(String roleName) {
        String normalized = roleName.trim().toUpperCase(Locale.ROOT);
        return normalized.startsWith("ROLE_") ? normalized : "ROLE_" + normalized;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
