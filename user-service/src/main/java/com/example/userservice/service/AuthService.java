package com.example.userservice.service;

import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.request.RefreshTokenRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.dto.response.AuthResponse;
import com.example.userservice.entity.RefreshToken;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.exception.BusinessException;
import com.example.userservice.repository.RefreshTokenRepository;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email đã được sử dụng", HttpStatus.CONFLICT);
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("Username đã được sử dụng", HttpStatus.CONFLICT);
        }

        Role userRole = roleRepository.findByName(Role.RoleName.BIDDER)
                .orElseThrow(() -> new BusinessException("Role không tồn tại", HttpStatus.INTERNAL_SERVER_ERROR));

        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .fullname(request.fullName())
                .phone(request.phone())
                .isActive(true)
                .roles(Collections.singleton(userRole))
                .build();

        user = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshTokenString = jwtService.generateRefreshToken(user);

        saveRefreshToken(user, refreshTokenString);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenString)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration)
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.usernameOrEmail(),
                            request.password()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BusinessException("Tài khoản hoặc mật khẩu không chính xác", HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByUsernameOrEmail(request.usernameOrEmail(), request.usernameOrEmail())
                .orElseThrow(() -> new BusinessException("Không tìm thấy người dùng", HttpStatus.NOT_FOUND));

        if (!user.isActive()) {
            throw new BusinessException("Tài khoản đã bị vô hiệu hóa", HttpStatus.FORBIDDEN);
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshTokenString = jwtService.generateRefreshToken(user);

        saveRefreshToken(user, refreshTokenString);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenString)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration)
                .build();
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException("Refresh token không hợp lệ", HttpStatus.UNAUTHORIZED));

        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Refresh token đã hết hạn hoặc bị thu hồi", HttpStatus.UNAUTHORIZED);
        }

        User user = refreshToken.getUser();
        
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtService.generateAccessToken(user);
        String newRefreshTokenString = jwtService.generateRefreshToken(user);

        saveRefreshToken(user, newRefreshTokenString);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshTokenString)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration)
                .build();
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException("Refresh token không hợp lệ", HttpStatus.BAD_REQUEST));
        
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    private void saveRefreshToken(User user, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusSeconds(refreshExpiration / 1000))
                .isRevoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
    }
}
