package com.example.userservice.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
    @Size(max = 255, message = "Họ tên quá dài")
    String fullName,

    @Size(max = 20, message = "Số điện thoại quá dài")
    String phone
) {}
