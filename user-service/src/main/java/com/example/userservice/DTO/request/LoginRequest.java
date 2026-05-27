package com.example.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Username hoặc Email không được để trống")
    String usernameOrEmail,

    @NotBlank(message = "Password không được để trống")
    String password
) {}
