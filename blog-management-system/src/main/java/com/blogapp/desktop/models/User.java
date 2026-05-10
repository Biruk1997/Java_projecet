package com.blogapp.desktop.models;

import java.time.LocalDateTime;

/**
 * User model for desktop application
 */
public class User {
    private String userId;
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
    
    // Stats
    private Integer followersCount;
    private Integer followingCount;
    private Integer postsCount;
    private Integer totalViews;
    private Integer totalClaps;
    private Integer totalComments;
    
    // Constructors
    public User() {}
    
    public User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }
    
    public Boolean getIsPremium() { return isPremium; }
    public void setIsPremium(Boolean isPremium) { this.isPremium = isPremium; }
    
    public Boolean getIsAdmin() { return isAdmin; }
    public void setIsAdmin(Boolean isAdmin) { this.isAdmin = isAdmin; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Integer getFollowersCount() { return followersCount != null ? followersCount : 0; }
    public void setFollowersCount(Integer followersCount) { this.followersCount = followersCount; }
    
    public Integer getFollowingCount() { return followingCount != null ? followingCount : 0; }
    public void setFollowingCount(Integer followingCount) { this.followingCount = followingCount; }
    
    public Integer getPostsCount() { return postsCount != null ? postsCount : 0; }
    public void setPostsCount(Integer postsCount) { this.postsCount = postsCount; }
    
    public Integer getTotalViews() { return totalViews != null ? totalViews : 0; }
    public void setTotalViews(Integer totalViews) { this.totalViews = totalViews; }
    
    public Integer getTotalClaps() { return totalClaps != null ? totalClaps : 0; }
    public void setTotalClaps(Integer totalClaps) { this.totalClaps = totalClaps; }
    
    public Integer getTotalComments() { return totalComments != null ? totalComments : 0; }
    public void setTotalComments(Integer totalComments) { this.totalComments = totalComments; }
    
    public String getFullName() {
        if (firstname != null && lastname != null) {
            return firstname + " " + lastname;
        }
        return displayName != null ? displayName : username;
    }
    
    public String getAvatarUrl() {
        if (avatar != null && !avatar.isEmpty()) {
            return avatar;
        }
        return "https://api.dicebear.com/7.x/avataaars/svg?seed=" + username;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
