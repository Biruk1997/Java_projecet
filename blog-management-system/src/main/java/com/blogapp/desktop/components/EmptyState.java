package com.blogapp.desktop.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * EmptyState Component - Displays empty state with icon and message
 * Equivalent to React's empty state components
 */
public class EmptyState extends VBox {
    
    private String icon;
    private String message;
    private String actionText;
    private Runnable actionHandler;
    
    public EmptyState(String icon, String message) {
        this(icon, message, null, null);
    }
    
    public EmptyState(String icon, String message, String actionText, Runnable actionHandler) {
        this.icon = icon;
        this.message = message;
        this.actionText = actionText;
        this.actionHandler = actionHandler;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPrefSize(400, 300);
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("System", 64));
        
        // Message
        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("System", FontWeight.NORMAL, 16));
        messageLabel.setTextFill(Color.web("#666666"));
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        messageLabel.setAlignment(Pos.CENTER);
        
        this.getChildren().addAll(iconLabel, messageLabel);
        
        // Action button (optional)
        if (actionText != null && actionHandler != null) {
            Button actionBtn = new Button(actionText);
            actionBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 12px 24px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            );
            actionBtn.setOnAction(e -> actionHandler.run());
            
            this.getChildren().add(actionBtn);
        }
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
        ((Label) this.getChildren().get(0)).setText(icon);
    }
    
    public void setMessage(String message) {
        this.message = message;
        ((Label) this.getChildren().get(1)).setText(message);
    }
}
