package com.blogapp.desktop.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

/**
 * CommentForm Component - Form for adding/editing comments
 * Equivalent to React's CommentForm.jsx
 */
public class CommentForm extends VBox {
    
    private TextArea contentArea;
    private Label charCountLabel;
    private Button submitButton;
    private Button cancelButton;
    private Consumer<String> onSubmitHandler;
    private Runnable onCancelHandler;
    private String initialContent;
    private boolean isEditing;
    
    public CommentForm() {
        this("", false);
    }
    
    public CommentForm(String initialContent, boolean isEditing) {
        this.initialContent = initialContent;
        this.isEditing = isEditing;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setSpacing(12);
        this.setPadding(new Insets(15));
        this.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 8px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 8px;"
        );
        
        // Label
        Label titleLabel = new Label(isEditing ? "Edit Comment" : "Add a Comment");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web("#1a1a1a"));
        
        // Text area
        contentArea = new TextArea(initialContent);
        contentArea.setPromptText("Share your thoughts...");
        contentArea.setWrapText(true);
        contentArea.setPrefRowCount(4);
        contentArea.setStyle(
            "-fx-font-size: 13px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 6px; " +
            "-fx-background-radius: 6px;"
        );
        
        // Character count
        charCountLabel = new Label("0 / 1000");
        charCountLabel.setFont(Font.font("System", 11));
        charCountLabel.setTextFill(Color.web("#999999"));
        
        contentArea.textProperty().addListener((obs, oldVal, newVal) -> {
            int length = newVal.length();
            charCountLabel.setText(length + " / 1000");
            
            if (length > 1000) {
                charCountLabel.setTextFill(Color.web("#d32f2f"));
            } else {
                charCountLabel.setTextFill(Color.web("#999999"));
            }
            
            submitButton.setDisable(newVal.trim().isEmpty() || length > 1000);
        });
        
        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        submitButton = new Button(isEditing ? "Update" : "Post Comment");
        submitButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 8px 20px; " +
            "-fx-background-radius: 6px; " +
            "-fx-cursor: hand;"
        );
        submitButton.setDisable(initialContent.trim().isEmpty());
        
        submitButton.setOnAction(e -> {
            if (onSubmitHandler != null) {
                onSubmitHandler.accept(contentArea.getText());
                if (!isEditing) {
                    contentArea.clear();
                }
            }
        });
        
        if (isEditing) {
            cancelButton = new Button("Cancel");
            cancelButton.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: #666666; " +
                "-fx-font-size: 13px; " +
                "-fx-padding: 8px 20px; " +
                "-fx-background-radius: 6px; " +
                "-fx-cursor: hand;"
            );
            
            cancelButton.setOnAction(e -> {
                if (onCancelHandler != null) {
                    onCancelHandler.run();
                }
            });
            
            buttonBox.getChildren().addAll(cancelButton, submitButton);
        } else {
            buttonBox.getChildren().add(submitButton);
        }
        
        this.getChildren().addAll(titleLabel, contentArea, charCountLabel, buttonBox);
    }
    
    public void setOnSubmit(Consumer<String> handler) {
        this.onSubmitHandler = handler;
    }
    
    public void setOnCancel(Runnable handler) {
        this.onCancelHandler = handler;
    }
    
    public void clear() {
        contentArea.clear();
    }
    
    public String getContent() {
        return contentArea.getText();
    }
}
