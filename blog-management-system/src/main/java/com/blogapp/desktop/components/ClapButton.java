package com.blogapp.desktop.components;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * ClapButton Component - Interactive clap/like button with animation
 * Equivalent to React's ClapButton.jsx
 */
public class ClapButton extends HBox {
    
    private Label iconLabel;
    private Label countLabel;
    private int clapCount;
    private boolean hasClapped;
    private Runnable onClapHandler;
    
    public ClapButton(int initialCount, boolean hasClapped) {
        this.clapCount = initialCount;
        this.hasClapped = hasClapped;
        initializeUI();
    }
    
    public ClapButton(int initialCount, boolean hasClapped, Runnable onClapHandler) {
        this.clapCount = initialCount;
        this.hasClapped = hasClapped;
        this.onClapHandler = onClapHandler;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setSpacing(8);
        this.setAlignment(Pos.CENTER);
        this.setStyle(
            "-fx-background-color: " + (hasClapped ? "#f0f0ff" : "#f5f5f5") + "; " +
            "-fx-background-radius: 20px; " +
            "-fx-padding: 8px 16px; " +
            "-fx-cursor: hand;"
        );
        
        // Clap icon
        iconLabel = new Label("👏");
        iconLabel.setFont(Font.font("System", 18));
        
        // Count label
        countLabel = new Label(String.valueOf(clapCount));
        countLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        countLabel.setTextFill(hasClapped ? Color.web("#667eea") : Color.web("#666666"));
        
        this.getChildren().addAll(iconLabel, countLabel);
        
        // Hover effect
        this.setOnMouseEntered(e -> {
            this.setStyle(
                "-fx-background-color: " + (hasClapped ? "#e0e0ff" : "#eeeeee") + "; " +
                "-fx-background-radius: 20px; " +
                "-fx-padding: 8px 16px; " +
                "-fx-cursor: hand;"
            );
        });
        
        this.setOnMouseExited(e -> {
            this.setStyle(
                "-fx-background-color: " + (hasClapped ? "#f0f0ff" : "#f5f5f5") + "; " +
                "-fx-background-radius: 20px; " +
                "-fx-padding: 8px 16px; " +
                "-fx-cursor: hand;"
            );
        });
        
        // Click handler
        this.setOnMouseClicked(e -> handleClap());
    }
    
    private void handleClap() {
        // Animate
        animateClap();
        
        // Update state
        if (!hasClapped) {
            clapCount++;
            hasClapped = true;
        } else {
            clapCount = Math.max(0, clapCount + 1); // Can clap multiple times
        }
        
        // Update UI
        updateUI();
        
        // Call handler
        if (onClapHandler != null) {
            onClapHandler.run();
        }
    }
    
    private void animateClap() {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), iconLabel);
        scaleUp.setToX(1.3);
        scaleUp.setToY(1.3);
        
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), iconLabel);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        
        scaleUp.setOnFinished(e -> scaleDown.play());
        scaleUp.play();
    }
    
    private void updateUI() {
        countLabel.setText(String.valueOf(clapCount));
        countLabel.setTextFill(hasClapped ? Color.web("#667eea") : Color.web("#666666"));
        
        this.setStyle(
            "-fx-background-color: " + (hasClapped ? "#f0f0ff" : "#f5f5f5") + "; " +
            "-fx-background-radius: 20px; " +
            "-fx-padding: 8px 16px; " +
            "-fx-cursor: hand;"
        );
    }
    
    public void setClapCount(int count) {
        this.clapCount = count;
        updateUI();
    }
    
    public void setHasClapped(boolean hasClapped) {
        this.hasClapped = hasClapped;
        updateUI();
    }
    
    public int getClapCount() {
        return clapCount;
    }
    
    public boolean hasClapped() {
        return hasClapped;
    }
}
