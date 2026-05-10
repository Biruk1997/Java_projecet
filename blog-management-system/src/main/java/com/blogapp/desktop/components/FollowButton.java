package com.blogapp.desktop.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * FollowButton Component - Follow/Unfollow button
 * Equivalent to React's FollowButton.jsx
 */
public class FollowButton extends Button {
    
    private boolean isFollowing;
    private boolean isLoading;
    private Runnable onToggleHandler;
    
    public FollowButton(boolean isFollowing) {
        this.isFollowing = isFollowing;
        this.isLoading = false;
        initializeUI();
    }
    
    public FollowButton(boolean isFollowing, Runnable onToggleHandler) {
        this.isFollowing = isFollowing;
        this.isLoading = false;
        this.onToggleHandler = onToggleHandler;
        initializeUI();
    }
    
    private void initializeUI() {
        updateUI();
        
        // Click handler
        this.setOnAction(e -> handleToggle());
    }
    
    private void handleToggle() {
        if (isLoading) {
            return;
        }
        
        // Set loading state
        setLoading(true);
        
        // Toggle state
        isFollowing = !isFollowing;
        updateUI();
        
        // Call handler
        if (onToggleHandler != null) {
            onToggleHandler.run();
        }
        
        // Reset loading state after a delay (simulated async operation)
        new Thread(() -> {
            try {
                Thread.sleep(500);
                javafx.application.Platform.runLater(() -> setLoading(false));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    
    private void updateUI() {
        if (isLoading) {
            this.setText("Loading...");
            this.setDisable(true);
        } else {
            this.setText(isFollowing ? "Following" : "Follow");
            this.setDisable(false);
        }
        
        this.setFont(Font.font("System", FontWeight.BOLD, 13));
        this.setAlignment(Pos.CENTER);
        
        if (isFollowing) {
            // Following state - outlined button
            this.setStyle(
                "-fx-background-color: white; " +
                "-fx-text-fill: #667eea; " +
                "-fx-border-color: #667eea; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 20px; " +
                "-fx-background-radius: 20px; " +
                "-fx-padding: 8px 20px; " +
                "-fx-cursor: hand;"
            );
            
            // Hover effect for following
            this.setOnMouseEntered(e -> {
                if (!isLoading) {
                    this.setText("Unfollow");
                    this.setStyle(
                        "-fx-background-color: #ffebee; " +
                        "-fx-text-fill: #d32f2f; " +
                        "-fx-border-color: #d32f2f; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-padding: 8px 20px; " +
                        "-fx-cursor: hand;"
                    );
                }
            });
            
            this.setOnMouseExited(e -> {
                if (!isLoading) {
                    this.setText("Following");
                    this.setStyle(
                        "-fx-background-color: white; " +
                        "-fx-text-fill: #667eea; " +
                        "-fx-border-color: #667eea; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-padding: 8px 20px; " +
                        "-fx-cursor: hand;"
                    );
                }
            });
        } else {
            // Not following state - filled button
            this.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 20px; " +
                "-fx-background-radius: 20px; " +
                "-fx-padding: 8px 20px; " +
                "-fx-cursor: hand;"
            );
            
            // Hover effect for not following
            this.setOnMouseEntered(e -> {
                if (!isLoading) {
                    this.setStyle(
                        "-fx-background-color: linear-gradient(to right, #5568d3, #6a3f8f); " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-padding: 8px 20px; " +
                        "-fx-cursor: hand;"
                    );
                }
            });
            
            this.setOnMouseExited(e -> {
                if (!isLoading) {
                    this.setStyle(
                        "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-padding: 8px 20px; " +
                        "-fx-cursor: hand;"
                    );
                }
            });
        }
    }
    
    public void setFollowing(boolean following) {
        this.isFollowing = following;
        updateUI();
    }
    
    public void setLoading(boolean loading) {
        this.isLoading = loading;
        updateUI();
    }
    
    public boolean isFollowing() {
        return isFollowing;
    }
    
    public boolean isLoading() {
        return isLoading;
    }
}
