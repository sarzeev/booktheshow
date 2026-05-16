package com.sarjeev.booktheshow.mappers;

import com.sarjeev.booktheshow.entities.Role;
import com.sarjeev.booktheshow.entities.User;
import com.sarjeev.booktheshow.responses.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(roleNames(user.getRoles()))")
    UserResponse toResponse(User user);

    default Set<String> roleNames(Set<Role> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toUnmodifiableSet());
    }
}
