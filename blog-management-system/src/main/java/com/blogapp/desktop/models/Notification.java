package com.blogapp.desktop.models;

import java.time.LocalDateTime;

/**
 * Notification model for desktop application
 */
public class Notification {
    private String notificationId;
    private String userId;
    private String type; // FOLLOW, COMMENT, CLAP, MENTION
    private String message;
    private String relatedId; // post_id, comment_id, user_id
    private Boolean isRead;
    private LocalDateTime createdAt;
    
    // Relationships
    private User fromUser;
    private Post relatedPost;
    
    // Constructors
    public Notification() {}
    
    public Notification(String notificationId, String type, String message) {
        this.notificationId = notificationId;
        this.type = type;
        this.message = message;
        this.isRead = false;
    }
    
    // Getters and Setters
    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getRelatedId() { return relatedId; }
    public void setRelatedId(String relatedId) { this.relatedId = relatedId; }
    
    public Boolean getIsRead() { return isRead != null ? isRead : false; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public User getFromUser() { return fromUser; }
    public void setFromUser(User fromUser) { this.fromUser = fromUser; }
    
    public Post getRelatedPost() { return relatedPost; }
    public void setRelatedPost(Post relatedPost) { this.relatedPost = relatedPost; }
    
    // Helper methods
    public String getFormattedDate() {
        if (createdAt == null) return "";
        
        java.time.Duration duration = java.time.Duration.between(createdAt, LocalDateTime.now());
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + "m ago";
        if (hours < 24) return hours + "h ago";
        if (days < 7) return days + "d ago";
        return createdAt.toLocalDate().toString();
    }
    
    public String getIcon() {
        if (type == null) return "🔔";
        switch (type.toUpperCase()) {
            case "FOLLOW": return "👤";
            case "COMMENT": return "💬";
            case "CLAP": return "👏";
            case "MENTION": return "📢";
            default: return "🔔";
        }
    }
    
    @Override
    public String toString() {
        return "Notification{" +
                "notificationId='" + notificationId + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
