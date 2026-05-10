package com.blogapp.desktop.components;

import com.blogapp.desktop.models.Comment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.format.DateTimeFormatter;

/**
 * CommentItem Component - Displays a single comment
 * Equivalent to React's CommentItem.jsx
 */
public class CommentItem extends VBox {
    
    private Comment comment;
    private boolean isOwner;
    private Runnable onReplyHandler;
    private Runnable onEditHandler;
    private Runnable onDeleteHandler;
    
    public CommentItem(Comment comment, boolean isOwner) {
        this.comment = comment;
        this.isOwner = isOwner;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setPadding(new Insets(15));
        this.setSpacing(10);
        this.setStyle(
            "-fx-background-color: #fafafa; " +
            "-fx-background-radius: 8px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 8px;"
        );
        
        // Header (author info)
        HBox headerBox = createHeaderBox();
        this.getChildren().add(headerBox);
        
        // Content
        Label contentLabel = new Label(comment.getContent());
        contentLabel.setFont(Font.font("System", 14));
        contentLabel.setWrapText(true);
        contentLabel.setTextFill(Color.web("#333333"));
        this.getChildren().add(contentLabel);
        
        // Footer (actions)
        HBox footerBox = createFooterBox();
        this.getChildren().add(footerBox);
    }
    
    private HBox createHeaderBox() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        
        // Avatar
        Label avatarLabel = new Label(getInitials());
        avatarLabel.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 18px; " +
            "-fx-min-width: 36px; " +
            "-fx-min-height: 36px; " +
            "-fx-max-width: 36px; " +
            "-fx-max-height: 36px; " +
            "-fx-alignment: center; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 12px;"
        );
        
        // Author name
        Label authorLabel = new Label(comment.getAuthor().getFirstname() + " " + comment.getAuthor().getLastname());
        authorLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        authorLabel.setTextFill(Color.web("#1a1a1a"));
        
        // Timestamp
        String timeStr = comment.getCreatedAt() != null ? 
            formatRelativeTime(comment.getCreatedAt().toString()) : "";
        Label timeLabel = new Label("• " + timeStr);
        timeLabel.setFont(Font.font("System", 12));
        timeLabel.setTextFill(Color.web("#999999"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        box.getChildren().addAll(avatarLabel, authorLabel, timeLabel, spacer);
        
        // Edit/Delete buttons for owner
        if (isOwner) {
            Button editBtn = new Button("Edit");
            editBtn.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: #667eea; " +
                "-fx-font-size: 11px; " +
                "-fx-cursor: hand;"
            );
            editBtn.setOnAction(e -> {
                if (onEditHandler != null) onEditHandler.run();
            });
            
            Button deleteBtn = new Button("Delete");
            deleteBtn.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: #d32f2f; " +
                "-fx-font-size: 11px; " +
                "-fx-cursor: hand;"
            );
            deleteBtn.setOnAction(e -> {
                if (onDeleteHandler != null) onDeleteHandler.run();
            });
            
            box.getChildren().addAll(editBtn, deleteBtn);
        }
        
        return box;
    }
    
    private HBox createFooterBox() {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        
        // Reply button
        Button replyBtn = new Button("💬 Reply");
        replyBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 12px; " +
            "-fx-cursor: hand; " +
            "-fx-font-weight: bold;"
        );
        replyBtn.setOnAction(e -> {
            if (onReplyHandler != null) onReplyHandler.run();
        });
        
        box.getChildren().add(replyBtn);
        
        return box;
    }
    
    private String getInitials() {
        String first = comment.getAuthor().getFirstname() != null && !comment.getAuthor().getFirstname().isEmpty() ? 
            comment.getAuthor().getFirstname().substring(0, 1) : "";
        String last = comment.getAuthor().getLastname() != null && !comment.getAuthor().getLastname().isEmpty() ? 
            comment.getAuthor().getLastname().substring(0, 1) : "";
        return (first + last).toUpperCase();
    }
    
    private String formatRelativeTime(String timestamp) {
        // Simplified relative time formatting
        return "just now"; // TODO: Implement proper relative time
    }
    
    public void setOnReply(Runnable handler) {
        this.onReplyHandler = handler;
    }
    
    public void setOnEdit(Runnable handler) {
        this.onEditHandler = handler;
    }
    
    public void setOnDelete(Runnable handler) {
        this.onDeleteHandler = handler;
    }
    
    public Comment getComment() {
        return comment;
    }
}
