package com.blogapp.desktop.components;

import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * BookmarkButton Component - Toggle bookmark button
 * Equivalent to React's BookmarkButton.jsx
 */
public class BookmarkButton extends StackPane {
    
    private Label iconLabel;
    private boolean isBookmarked;
    private Runnable onToggleHandler;
    
    public BookmarkButton(boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
        initializeUI();
    }
    
    public BookmarkButton(boolean isBookmarked, Runnable onToggleHandler) {
        this.isBookmarked = isBookmarked;
        this.onToggleHandler = onToggleHandler;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setAlignment(Pos.CENTER);
        this.setStyle(
            "-fx-background-color: " + (isBookmarked ? "#fff3e0" : "#f5f5f5") + "; " +
            "-fx-background-radius: 50%; " +
            "-fx-min-width: 40px; " +
            "-fx-min-height: 40px; " +
            "-fx-max-width: 40px; " +
            "-fx-max-height: 40px; " +
            "-fx-cursor: hand;"
        );
        
        // Bookmark icon
        iconLabel = new Label(isBookmarked ? "🔖" : "📑");
        iconLabel.setFont(Font.font("System", 18));
        
        this.getChildren().add(iconLabel);
        
        // Tooltip
        Tooltip tooltip = new Tooltip(isBookmarked ? "Remove bookmark" : "Add bookmark");
        Tooltip.install(this, tooltip);
        
        // Hover effect
        this.setOnMouseEntered(e -> {
            this.setStyle(
                "-fx-background-color: " + (isBookmarked ? "#ffe0b2" : "#eeeeee") + "; " +
                "-fx-background-radius: 50%; " +
                "-fx-min-width: 40px; " +
                "-fx-min-height: 40px; " +
                "-fx-max-width: 40px; " +
                "-fx-max-height: 40px; " +
                "-fx-cursor: hand;"
            );
        });
        
        this.setOnMouseExited(e -> {
            this.setStyle(
                "-fx-background-color: " + (isBookmarked ? "#fff3e0" : "#f5f5f5") + "; " +
                "-fx-background-radius: 50%; " +
                "-fx-min-width: 40px; " +
                "-fx-min-height: 40px; " +
                "-fx-max-width: 40px; " +
                "-fx-max-height: 40px; " +
                "-fx-cursor: hand;"
            );
        });
        
        // Click handler
        this.setOnMouseClicked(e -> handleToggle());
    }
    
    private void handleToggle() {
        // Animate
        animateToggle();
        
        // Toggle state
        isBookmarked = !isBookmarked;
        
        // Update UI
        updateUI();
        
        // Call handler
        if (onToggleHandler != null) {
            onToggleHandler.run();
        }
    }
    
    private void animateToggle() {
        RotateTransition rotate = new RotateTransition(Duration.millis(200), iconLabel);
        rotate.setByAngle(360);
        rotate.play();
    }
    
    private void updateUI() {
        iconLabel.setText(isBookmarked ? "🔖" : "📑");
        
        this.setStyle(
            "-fx-background-color: " + (isBookmarked ? "#fff3e0" : "#f5f5f5") + "; " +
            "-fx-background-radius: 50%; " +
            "-fx-min-width: 40px; " +
            "-fx-min-height: 40px; " +
            "-fx-max-width: 40px; " +
            "-fx-max-height: 40px; " +
            "-fx-cursor: hand;"
        );
        
        Tooltip tooltip = new Tooltip(isBookmarked ? "Remove bookmark" : "Add bookmark");
        Tooltip.install(this, tooltip);
    }
    
    public void setBookmarked(boolean bookmarked) {
        this.isBookmarked = bookmarked;
        updateUI();
    }
    
    public boolean isBookmarked() {
        return isBookmarked;
    }
}
