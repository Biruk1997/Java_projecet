package com.blogapp.desktop.utils;

import java.util.prefs.Preferences;

/**
 * SessionManager - Session and token management
 * Equivalent to React's localStorage for session data
 */
public class SessionManager {
    
    private static final Preferences prefs = Preferences.userNodeForPackage(SessionManager.class);
    
    private static final String TOKEN_KEY = "auth_token";
    private static final String USER_ID_KEY = "user_id";
    private static final String USERNAME_KEY = "username";
    private static final String EMAIL_KEY = "email";
    
    /**
     * Save authentication token
     */
    public static void saveToken(String token) {
        if (token != null) {
            prefs.put(TOKEN_KEY, token);
        } else {
            prefs.remove(TOKEN_KEY);
        }
    }
    
    /**
     * Get authentication token
     */
    public static String getToken() {
        return prefs.get(TOKEN_KEY, null);
    }
    
    /**
     * Save user ID
     */
    public static void saveUserId(String userId) {
        if (userId != null) {
            prefs.put(USER_ID_KEY, userId);
        } else {
            prefs.remove(USER_ID_KEY);
        }
    }
    
    /**
     * Get user ID
     */
    public static String getUserId() {
        return prefs.get(USER_ID_KEY, null);
    }
    
    /**
     * Save username
     */
    public static void saveUsername(String username) {
        if (username != null) {
            prefs.put(USERNAME_KEY, username);
        } else {
            prefs.remove(USERNAME_KEY);
        }
    }
    
    /**
     * Get username
     */
    public static String getUsername() {
        return prefs.get(USERNAME_KEY, null);
    }
    
    /**
     * Save email
     */
    public static void saveEmail(String email) {
        if (email != null) {
            prefs.put(EMAIL_KEY, email);
        } else {
            prefs.remove(EMAIL_KEY);
        }
    }
    
    /**
     * Get email
     */
    public static String getEmail() {
        return prefs.get(EMAIL_KEY, null);
    }
    
    /**
     * Check if user is authenticated
     */
    public static boolean isAuthenticated() {
        return getToken() != null && getUserId() != null;
    }
    
    /**
     * Clear all session data (logout)
     */
    public static void clearSession() {
        prefs.remove(TOKEN_KEY);
        prefs.remove(USER_ID_KEY);
        prefs.remove(USERNAME_KEY);
        prefs.remove(EMAIL_KEY);
    }
    
    /**
     * Save complete session
     */
    public static void saveSession(String token, String userId, String username, String email) {
        saveToken(token);
        saveUserId(userId);
        saveUsername(username);
        saveEmail(email);
    }
}
