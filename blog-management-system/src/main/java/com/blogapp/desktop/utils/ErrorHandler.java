package com.blogapp.desktop.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * ErrorHandler - Centralized error handling and display
 */
public class ErrorHandler {
    
    /**
     * Show error alert
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show error from exception
     */
    public static void showError(String title, Exception e) {
        showError(title, e.getMessage() != null ? e.getMessage() : "An error occurred");
    }
    
    /**
     * Show warning alert
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show info alert
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show success alert
     */
    public static void showSuccess(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Log error to console
     */
    public static void logError(String message, Exception e) {
        System.err.println("[ERROR] " + message);
        if (e != null) {
            e.printStackTrace();
        }
    }
    
    /**
     * Handle API error
     */
    public static void handleApiError(Exception e) {
        String message = e.getMessage();
        if (message == null || message.isEmpty()) {
            message = "An unexpected error occurred";
        }
        
        if (message.contains("401") || message.contains("Unauthorized")) {
            showError("Authentication Error", "Your session has expired. Please login again.");
        } else if (message.contains("403") || message.contains("Forbidden")) {
            showError("Access Denied", "You don't have permission to perform this action.");
        } else if (message.contains("404") || message.contains("Not Found")) {
            showError("Not Found", "The requested resource was not found.");
        } else if (message.contains("500") || message.contains("Internal Server Error")) {
            showError("Server Error", "A server error occurred. Please try again later.");
        } else {
            showError("Error", message);
        }
    }
}
