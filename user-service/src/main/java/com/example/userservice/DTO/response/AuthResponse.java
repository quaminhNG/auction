package com.example.userservice.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn
) {}
