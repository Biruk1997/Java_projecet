package com.blogapp.desktop.components;

import com.blogapp.desktop.models.Topic;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * TopicChip Component - Displays a topic tag/chip
 * Equivalent to React's topic tags
 */
public class TopicChip extends HBox {
    
    private Topic topic;
    private boolean isClickable;
    private boolean isRemovable;
    private Runnable onClickHandler;
    private Runnable onRemoveHandler;
    
    public TopicChip(Topic topic) {
        this(topic, false, false);
    }
    
    public TopicChip(Topic topic, boolean isClickable, boolean isRemovable) {
        this.topic = topic;
        this.isClickable = isClickable;
        this.isRemovable = isRemovable;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(6);
        this.setStyle(
            "-fx-background-color: #f0f0f0; " +
            "-fx-background-radius: 16px; " +
            "-fx-padding: 6px 14px; " +
            (isClickable ? "-fx-cursor: hand;" : "")
        );
        
        // Topic name
        Label nameLabel = new Label(topic.getName());
        nameLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        nameLabel.setTextFill(Color.web("#666666"));
        
        this.getChildren().add(nameLabel);
        
        // Remove button (if removable)
        if (isRemovable) {
            Label removeLabel = new Label("×");
            removeLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            removeLabel.setTextFill(Color.web("#999999"));
            removeLabel.setStyle("-fx-cursor: hand;");
            
            removeLabel.setOnMouseClicked(e -> {
                if (onRemoveHandler != null) {
                    onRemoveHandler.run();
                }
            });
            
            removeLabel.setOnMouseEntered(e -> {
                removeLabel.setTextFill(Color.web("#d32f2f"));
            });
            
            removeLabel.setOnMouseExited(e -> {
                removeLabel.setTextFill(Color.web("#999999"));
            });
            
            this.getChildren().add(removeLabel);
        }
        
        // Hover effect (if clickable)
        if (isClickable) {
            this.setOnMouseEntered(e -> {
                this.setStyle(
                    "-fx-background-color: #e0e0e0; " +
                    "-fx-background-radius: 16px; " +
                    "-fx-padding: 6px 14px; " +
                    "-fx-cursor: hand;"
                );
            });
            
            this.setOnMouseExited(e -> {
                this.setStyle(
                    "-fx-background-color: #f0f0f0; " +
                    "-fx-background-radius: 16px; " +
                    "-fx-padding: 6px 14px; " +
                    "-fx-cursor: hand;"
                );
            });
            
            this.setOnMouseClicked(e -> {
                if (onClickHandler != null) {
                    onClickHandler.run();
                }
            });
        }
    }
    
    public void setOnClick(Runnable handler) {
        this.onClickHandler = handler;
    }
    
    public void setOnRemove(Runnable handler) {
        this.onRemoveHandler = handler;
    }
    
    public Topic getTopic() {
        return topic;
    }
}
