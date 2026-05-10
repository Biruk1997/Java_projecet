package com.blogapp.service;

import com.blogapp.dto.UserDTO;
import com.blogapp.dto.request.LoginRequest;
import com.blogapp.dto.request.SignupRequest;
import com.blogapp.dto.response.AuthResponse;
import com.blogapp.entity.User;
import com.blogapp.exception.ResourceAlreadyExistsException;
import com.blogapp.exception.UnauthorizedException;
import com.blogapp.repository.UserRepository;
import com.blogapp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists");
        }
        
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .displayName(request.getDisplayName())
                .password(passwordEncoder.encode(request.getPassword()))
                .emailVerified(false)
                .isPremium(false)
                .isAdmin(false)
                .isActive(true)
                .build();
        
        user = userRepository.save(user);
        
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getIsAdmin());
        
        UserDTO userDTO = mapToUserDTO(user);
        
        return AuthResponse.builder()
                .user(userDTO)
                .accessToken(token)
                .build();
    }
    
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        
        if (!user.getIsActive()) {
            throw new UnauthorizedException("Account is inactive");
        }
        
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getIsAdmin());
        
        UserDTO userDTO = mapToUserDTO(user);
        
        return AuthResponse.builder()
                .user(userDTO)
                .accessToken(token)
                .build();
    }
    
    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .avatar(user.getAvatar())
                .bio(user.getBio())
                .emailVerified(user.getEmailVerified())
                .isPremium(user.getIsPremium())
                .isAdmin(user.getIsAdmin())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
