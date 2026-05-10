package com.blogapp.desktop.services;

import com.blogapp.desktop.models.Notification;
import com.blogapp.desktop.utils.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Notification Service - Handles notification operations
 * Equivalent to React's notificationService.jsx
 */
public class NotificationService {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * Get all notifications for current user
     */
    public static CompletableFuture<List<Notification>> getNotifications(String token) {
        return HttpClientUtil.get("/notifications", token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Notification>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse notifications", e);
                    }
                });
    }
    
    /**
     * Get unread notifications
     */
    public static CompletableFuture<List<Notification>> getUnreadNotifications(String token) {
        return HttpClientUtil.get("/notifications/unread", token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Notification>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse unread notifications", e);
                    }
                });
    }
    
    /**
     * Mark notification as read
     */
    public static CompletableFuture<Void> markAsRead(String notificationId, String token) {
        Map<String, Boolean> body = new HashMap<>();
        body.put("isRead", true);
        
        return HttpClientUtil.put("/notifications/" + notificationId + "/read", body, token, Void.class)
                .thenApply(v -> null);
    }
    
    /**
     * Mark all notifications as read
     */
    public static CompletableFuture<Void> markAllAsRead(String token) {
        return HttpClientUtil.put("/notifications/read-all", null, token, Void.class)
                .thenApply(v -> null);
    }
    
    /**
     * Get unread notification count
     */
    public static CompletableFuture<Integer> getUnreadCount(String token) {
        return HttpClientUtil.get("/notifications/unread/count", token, UnreadCountResponse.class)
                .thenApply(response -> response.count);
    }
    
    /**
     * Delete notification
     */
    public static CompletableFuture<Void> deleteNotification(String notificationId, String token) {
        return HttpClientUtil.delete("/notifications/" + notificationId, token);
    }
    
    /**
     * Delete all notifications
     */
    public static CompletableFuture<Void> deleteAllNotifications(String token) {
        return HttpClientUtil.delete("/notifications/all", token);
    }
    
    /**
     * Get notifications by type
     */
    public static CompletableFuture<List<Notification>> getNotificationsByType(String type, String token) {
        return HttpClientUtil.get("/notifications/type/" + type, token, String.class)
                .thenApply(json -> {
                    try {
                        return mapper.readValue(json, new TypeReference<List<Notification>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse notifications by type", e);
                    }
                });
    }
    
    // DTOs
    public static class UnreadCountResponse {
        public int count;
    }
}
