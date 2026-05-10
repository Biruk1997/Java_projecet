package com.blogapp.dto;

import com.blogapp.entity.Notification;
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
public class NotificationDTO {
    private UUID notificationId;
    private Notification.NotificationType type;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private UserDTO actor;
    private UUID postId;
    private UUID commentId;
}
