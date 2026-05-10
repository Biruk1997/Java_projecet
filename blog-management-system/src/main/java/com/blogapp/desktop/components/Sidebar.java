package com.blogapp.desktop.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Sidebar Component - Side navigation menu
 * Equivalent to React's sidebar navigation
 */

public class Sidebar extends VBox {
    private String username;
    private Runnable onHomeHandler;
    private Runnable onWriteHandler;
    private Runnable onDraftsHandler;
    private Runnable onBookmarksHandler;
    private Runnable onFollowersHandler;
    private Runnable onFollowingHandler;
    private Runnable onDiscoverHandler;
    private Runnable onProfileHandler;
    private Runnable onSettingsHandler;
    
    public Sidebar(String username) {
        this.username = username;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setPrefWidth(250);
        this.setSpacing(0);
        this.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 0 1px 0 0;"
        );
        
        VBox content = new VBox(0);
        content.setPadding(new Insets(20, 15, 20, 15));
        
        // Profile section
        VBox profileSection = createProfileSection();
        content.getChildren().add(profileSection);
        
        // Separator
        VBox separator = new VBox();
        separator.setPrefHeight(1);
        separator.setStyle("-fx-background-color: #e0e0e0;");
        separator.setPadding(new Insets(15, 0, 15, 0));
        content.getChildren().add(separator);
        
        // Navigation section
        Label navLabel = new Label("MENU");
        navLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        navLabel.setTextFill(Color.web("#999999"));
        navLabel.setPadding(new Insets(0, 0, 10, 10));
        content.getChildren().add(navLabel);
        
        content.getChildren().addAll(
            createNavButton("🏠 Home Feed", true, () -> {
                if (onHomeHandler != null) onHomeHandler.run();
            }),
            createNavButton("✍️ Write Story", false, () -> {
                if (onWriteHandler != null) onWriteHandler.run();
            }),
            createNavButton("📄 My Drafts", false, () -> {
                if (onDraftsHandler != null) onDraftsHandler.run();
            }),
            createNavButton("🔖 Bookmarks", false, () -> {
                if (onBookmarksHandler != null) onBookmarksHandler.run();
            }),
            createNavButton("👥 Followers", false, () -> {
                if (onFollowersHandler != null) onFollowersHandler.run();
            }),
            createNavButton("✨ Following", false, () -> {
                if (onFollowingHandler != null) onFollowingHandler.run();
            }),
            createNavButton("� Discover Users", false, () -> {
                if (onDiscoverHandler != null) onDiscoverHandler.run();
            }),
            createNavButton("�👤 My Profile", false, () -> {
                if (onProfileHandler != null) onProfileHandler.run();
            }),
            createNavButton("⚙️ Settings", false, () -> {
                if (onSettingsHandler != null) onSettingsHandler.run();
            })
        );
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white; -fx-background-color: white;");
        
        this.getChildren().add(scrollPane);
    }
    
    private VBox createProfileSection() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(0, 0, 10, 0));
        box.setAlignment(Pos.CENTER_LEFT);
        
        // Avatar
        Label avatarLabel = new Label(getInitials(username));
        avatarLabel.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 25px; " +
            "-fx-min-width: 50px; " +
            "-fx-min-height: 50px; " +
            "-fx-max-width: 50px; " +
            "-fx-max-height: 50px; " +
            "-fx-alignment: center; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 18px;"
        );
        
        // Username
        Label usernameLabel = new Label(username);
        usernameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        usernameLabel.setTextFill(Color.web("#1a1a1a"));
        
        Label statusLabel = new Label("● Active");
        statusLabel.setFont(Font.font("System", 11));
        statusLabel.setTextFill(Color.web("#4caf50"));
        
        box.getChildren().addAll(avatarLabel, usernameLabel, statusLabel);
        return box;
    }
    
    private Button createNavButton(String text, boolean isActive, Runnable handler) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(12, 15, 12, 15));
        
        if (isActive) {
            btn.setStyle(
                "-fx-background-color: #f0f0ff; " +
                "-fx-text-fill: #667eea; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            );
        } else {
            btn.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: #666666; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            );
            
            btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #f5f5f5; " +
                "-fx-text-fill: #1a1a1a; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            ));
            
            btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: #666666; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            ));
        }
        
        btn.setOnAction(e -> {
            if (handler != null) handler.run();
        });
        
        return btn;
    }
    
    private String getInitials(String username) {
        if (username == null || username.isEmpty()) return "U";
        return username.substring(0, Math.min(2, username.length())).toUpperCase();
    }
    
    public void setOnHome(Runnable handler) { this.onHomeHandler = handler; }
    public void setOnWrite(Runnable handler) { this.onWriteHandler = handler; }
    public void setOnDrafts(Runnable handler) { this.onDraftsHandler = handler; }
    public void setOnBookmarks(Runnable handler) { this.onBookmarksHandler = handler; }
    public void setOnFollowers(Runnable handler) { this.onFollowersHandler = handler; }
    public void setOnFollowing(Runnable handler) { this.onFollowingHandler = handler; }
    public void setOnDiscover(Runnable handler) { this.onDiscoverHandler = handler; }
    public void setOnProfile(Runnable handler) { this.onProfileHandler = handler; }
    public void setOnSettings(Runnable handler) { this.onSettingsHandler = handler; }
}
