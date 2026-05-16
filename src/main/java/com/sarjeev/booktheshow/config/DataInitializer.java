package com.sarjeev.booktheshow.config;

import com.sarjeev.booktheshow.entities.Role;
import com.sarjeev.booktheshow.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private static final List<String> REQUIRED_ROLES = List.of(
            "ROLE_ADMIN",
            "ROLE_ORGANIZER",
            "ROLE_ATTENDEE",
            "ROLE_STAFF"
    );

    private final RoleRepository roleRepository;

    @Bean
    public CommandLineRunner seedRoles() {
        return args -> REQUIRED_ROLES.stream()
                .filter(roleName -> !roleRepository.existsByName(roleName))
                .map(roleName -> Role.builder().name(roleName).build())
                .forEach(roleRepository::save);
    }
}
