package com.blogapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID userId;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String displayName;
    private String avatar;
    private String bio;
    private Boolean emailVerified;
    private Boolean isPremium;
    private Boolean isAdmin;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer followersCount;
    private Integer followingCount;
    private Integer postsCount;
}
