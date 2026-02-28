package com.example.userservice.service.impl;

import com.example.userservice.dto.request.UpdateProfileRequest;
import com.example.userservice.dto.response.UserProfileDto;
import com.example.userservice.entity.User;
import com.example.userservice.exception.BusinessException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUser(String username) {
        User user = getUserByUsername(username);
        return mapToProfileDto(user);
    }

    @Override
    @Transactional
    public UserProfileDto updateUserProfile(String username, UpdateProfileRequest request) {
        User user = getUserByUsername(username);

        if (request.fullName() != null) {
            user.setFullname(request.fullName());
        }
        if (request.phone() != null) {
            user.setPhone(request.phone());
        }

        user = userRepository.save(user);
        return mapToProfileDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy người dùng", HttpStatus.NOT_FOUND));
        return mapToProfileDto(user);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Không tìm thấy người dùng", HttpStatus.NOT_FOUND));
    }

    private UserProfileDto mapToProfileDto(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return new UserProfileDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFullname(),
                user.getPhone(),
                user.isActive(),
                roles
        );
    }
}
