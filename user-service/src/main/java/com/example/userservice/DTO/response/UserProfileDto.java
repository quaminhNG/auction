package com.example.userservice.dto.response;

import java.util.Set;
import java.util.UUID;

public record UserProfileDto(
    UUID id,
    String email,
    String username,
    String fullName,
    String phone,
    boolean isActive,
    Set<String> roles
) {}
