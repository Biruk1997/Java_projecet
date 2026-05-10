package com.blogapp.desktop.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Avatar Component - User avatar with fallback to generated initials
 * Equivalent to React's avatar components
 */
public class Avatar extends StackPane {
    
    private String imageUrl;
    private String username;
    private int size;
    
    public Avatar(String imageUrl, String username, int size) {
        this.imageUrl = imageUrl;
        this.username = username;
        this.size = size;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setAlignment(Pos.CENTER);
        this.setPrefSize(size, size);
        this.setMaxSize(size, size);
        this.setMinSize(size, size);
        
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                ImageView imageView = new ImageView(new Image(imageUrl, true));
                imageView.setFitWidth(size);
                imageView.setFitHeight(size);
                imageView.setPreserveRatio(true);
                imageView.setStyle(
                    "-fx-background-radius: " + (size / 2) + "px; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
                );
                this.getChildren().add(imageView);
                return;
            } catch (Exception e) {
                // Fall through to initials
            }
        }
        
        // Fallback to initials
        Label initialsLabel = new Label(getInitials());
        initialsLabel.setFont(Font.font("System", FontWeight.BOLD, size / 2.5));
        initialsLabel.setTextFill(Color.WHITE);
        initialsLabel.setAlignment(Pos.CENTER);
        
        this.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-background-radius: " + (size / 2) + "px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        
        this.getChildren().add(initialsLabel);
    }
    
    private String getInitials() {
        if (username == null || username.isEmpty()) {
            return "U";
        }
        
        String[] parts = username.split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        } else {
            return username.substring(0, Math.min(2, username.length())).toUpperCase();
        }
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.getChildren().clear();
        initializeUI();
    }
}
