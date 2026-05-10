package com.blogapp.desktop.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * DateFormatter - Date formatting utilities
 * Equivalent to React's date formatting functions
 */
public class DateFormatter {
    
    private static final DateTimeFormatter FULL_FORMAT = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");
    private static final DateTimeFormatter SHORT_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");
    
    /**
     * Format date as relative time (e.g., "2 hours ago")
     */
    public static String formatRelativeTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        
        long seconds = ChronoUnit.SECONDS.between(dateTime, now);
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        long hours = ChronoUnit.HOURS.between(dateTime, now);
        long days = ChronoUnit.DAYS.between(dateTime, now);
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;
        
        if (seconds < 60) {
            return "just now";
        } else if (minutes < 60) {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else if (hours < 24) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (days < 7) {
            return days + (days == 1 ? " day ago" : " days ago");
        } else if (weeks < 4) {
            return weeks + (weeks == 1 ? " week ago" : " weeks ago");
        } else if (months < 12) {
            return months + (months == 1 ? " month ago" : " months ago");
        } else {
            return years + (years == 1 ? " year ago" : " years ago");
        }
    }
    
    /**
     * Format date as full date and time
     */
    public static String formatFullDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(FULL_FORMAT);
    }
    
    /**
     * Format date as short date
     */
    public static String formatShortDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(SHORT_FORMAT);
    }
    
    /**
     * Format time only
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(TIME_FORMAT);
    }
    
    /**
     * Check if date is today
     */
    public static boolean isToday(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return dateTime.toLocalDate().equals(now.toLocalDate());
    }
    
    /**
     * Check if date is this week
     */
    public static boolean isThisWeek(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        long days = ChronoUnit.DAYS.between(dateTime, now);
        return days < 7;
    }
}
