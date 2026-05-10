package com.blogapp.desktop.utils;

import java.util.regex.Pattern;

/**
 * ValidationUtils - Form validation utilities
 * Equivalent to React's validation.js
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );
    
    /**
     * Validate email address
     */
    public static ValidationResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new ValidationResult(false, "Email is required");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new ValidationResult(false, "Invalid email format");
        }
        
        return new ValidationResult(true, null);
    }
    
    /**
     * Validate password
     */
    public static ValidationResult validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return new ValidationResult(false, "Password is required");
        }
        
        if (password.length() < 8) {
            return new ValidationResult(false, "Password must be at least 8 characters");
        }
        
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);
        
        if (!hasUpper) {
            return new ValidationResult(false, "Password must contain at least one uppercase letter");
        }
        if (!hasLower) {
            return new ValidationResult(false, "Password must contain at least one lowercase letter");
        }
        if (!hasDigit) {
            return new ValidationResult(false, "Password must contain at least one number");
        }
        if (!hasSpecial) {
            return new ValidationResult(false, "Password must contain at least one special character");
        }
        
        return new ValidationResult(true, null);
    }
    
    /**
     * Validate username
     */
    public static ValidationResult validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ValidationResult(false, "Username is required");
        }
        
        if (username.length() < 3) {
            return new ValidationResult(false, "Username must be at least 3 characters");
        }
        
        if (username.length() > 20) {
            return new ValidationResult(false, "Username must be at most 20 characters");
        }
        
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return new ValidationResult(false, "Username can only contain letters, numbers, and underscores");
        }
        
        return new ValidationResult(true, null);
    }
    
    /**
     * Validate name (first name, last name)
     */
    public static ValidationResult validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            return new ValidationResult(false, fieldName + " is required");
        }
        
        if (name.length() < 2) {
            return new ValidationResult(false, fieldName + " must be at least 2 characters");
        }
        
        if (name.length() > 50) {
            return new ValidationResult(false, fieldName + " must be at most 50 characters");
        }
        
        return new ValidationResult(true, null);
    }
    
    /**
     * Get password strength
     */
    public static PasswordStrength getPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return new PasswordStrength(0, "Too weak", "#d32f2f");
        }
        
        int score = 0;
        
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;
        if (password.chars().anyMatch(Character::isUpperCase)) score++;
        if (password.chars().anyMatch(Character::isLowerCase)) score++;
        if (password.chars().anyMatch(Character::isDigit)) score++;
        if (password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0)) score++;
        
        if (score <= 2) {
            return new PasswordStrength(score, "Weak", "#d32f2f");
        } else if (score <= 4) {
            return new PasswordStrength(score, "Medium", "#ff9800");
        } else {
            return new PasswordStrength(score, "Strong", "#4caf50");
        }
    }
    
    // Result classes
    public static class ValidationResult {
        public final boolean isValid;
        public final String error;
        
        public ValidationResult(boolean isValid, String error) {
            this.isValid = isValid;
            this.error = error;
        }
    }
    
    public static class PasswordStrength {
        public final int score;
        public final String strength;
        public final String color;
        
        public PasswordStrength(int score, String strength, String color) {
            this.score = score;
            this.strength = strength;
            this.color = color;
        }
    }
}
