package com.blogapp.desktop.components;

import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * LoadingSpinner Component - Animated loading indicator
 * Equivalent to React's loading states
 */
public class LoadingSpinner extends VBox {
    
    private Label spinnerLabel;
    private Label messageLabel;
    private RotateTransition rotateTransition;
    
    public LoadingSpinner() {
        this("Loading...");
    }
    
    public LoadingSpinner(String message) {
        initializeUI(message);
    }
    
    private void initializeUI(String message) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(15);
        this.setPrefSize(200, 150);
        
        // Spinner icon
        spinnerLabel = new Label("⏳");
        spinnerLabel.setFont(Font.font("System", 48));
        
        // Rotate animation
        rotateTransition = new RotateTransition(Duration.seconds(2), spinnerLabel);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.play();
        
        // Message
        messageLabel = new Label(message);
        messageLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        messageLabel.setTextFill(Color.web("#666666"));
        
        this.getChildren().addAll(spinnerLabel, messageLabel);
    }
    
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
    
    public void stop() {
        if (rotateTransition != null) {
            rotateTransition.stop();
        }
    }
}
