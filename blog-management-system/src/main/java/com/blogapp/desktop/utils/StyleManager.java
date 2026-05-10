package com.blogapp.desktop.utils;

/**
 * StyleManager - CSS styling utilities
 */
public class StyleManager {
    
    // Color constants
    public static final String PRIMARY_GRADIENT = "linear-gradient(to right, #667eea, #764ba2)";
    public static final String PRIMARY_COLOR = "#667eea";
    public static final String SECONDARY_COLOR = "#764ba2";
    public static final String SUCCESS_COLOR = "#4caf50";
    public static final String ERROR_COLOR = "#d32f2f";
    public static final String WARNING_COLOR = "#ff9800";
    public static final String INFO_COLOR = "#2196f3";
    
    public static final String BG_PRIMARY = "#ffffff";
    public static final String BG_SECONDARY = "#f5f5f5";
    public static final String BG_TERTIARY = "#fafafa";
    
    public static final String TEXT_PRIMARY = "#1a1a1a";
    public static final String TEXT_SECONDARY = "#666666";
    public static final String TEXT_TERTIARY = "#999999";
    
    /**
     * Get button style
     */
    public static String getButtonStyle(boolean isPrimary) {
        if (isPrimary) {
            return "-fx-background-color: " + PRIMARY_GRADIENT + "; " +
                   "-fx-text-fill: white; " +
                   "-fx-font-size: 14px; " +
                   "-fx-font-weight: bold; " +
                   "-fx-padding: 10px 20px; " +
                   "-fx-background-radius: 8px; " +
                   "-fx-cursor: hand;";
        } else {
            return "-fx-background-color: transparent; " +
                   "-fx-text-fill: " + TEXT_SECONDARY + "; " +
                   "-fx-border-color: " + TEXT_SECONDARY + "; " +
                   "-fx-border-width: 1px; " +
                   "-fx-font-size: 14px; " +
                   "-fx-padding: 10px 20px; " +
                   "-fx-background-radius: 8px; " +
                   "-fx-cursor: hand;";
        }
    }
    
    /**
     * Get card style
     */
    public static String getCardStyle() {
        return "-fx-background-color: white; " +
               "-fx-background-radius: 12px; " +
               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);";
    }
    
    /**
     * Get input field style
     */
    public static String getInputStyle() {
        return "-fx-font-size: 14px; " +
               "-fx-padding: 10px; " +
               "-fx-border-color: #e0e0e0; " +
               "-fx-border-radius: 8px; " +
               "-fx-background-radius: 8px;";
    }
}
