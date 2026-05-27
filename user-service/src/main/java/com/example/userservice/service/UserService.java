package com.example.userservice.service;

import com.example.userservice.dto.request.UpdateProfileRequest;
import com.example.userservice.dto.response.UserProfileDto;

import java.util.UUID;

public interface UserService {
    UserProfileDto getCurrentUser(String username);
    UserProfileDto updateUserProfile(String username, UpdateProfileRequest request);
    UserProfileDto getUserById(UUID id);
}
