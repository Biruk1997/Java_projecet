package com.blogapp.desktop.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * NavigationBar Component - Top navigation bar
 * Equivalent to React's navigation header
 */
public class NavigationBar extends HBox {
    
    private TextField searchField;
    private Label notificationBadge;
    private Runnable onSearchHandler;
    private Runnable onWriteHandler;
    private Runnable onNotificationsHandler;
    private Runnable onProfileHandler;
    private Runnable onLogoutHandler;
    
    public NavigationBar(String username) {
        initializeUI(username);
    }
    
    private void initializeUI(String username) {
        this.setPadding(new Insets(12, 20, 12, 20));
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle(
            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);"
        );
        
        // Logo/Brand
        Label brandLabel = new Label("📝 Blog Platform");
        brandLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        brandLabel.setTextFill(Color.WHITE);
        brandLabel.setStyle("-fx-cursor: hand;");
        
        // Search bar
        searchField = new TextField();
        searchField.setPromptText("Search posts, users, topics...");
        searchField.setPrefWidth(300);
        searchField.setStyle(
            "-fx-background-color: rgba(255,255,255,0.2); " +
            "-fx-text-fill: white; " +
            "-fx-prompt-text-fill: rgba(255,255,255,0.7); " +
            "-fx-background-radius: 20px; " +
            "-fx-padding: 8px 16px; " +
            "-fx-font-size: 13px;"
        );
        
        searchField.setOnAction(e -> {
            if (onSearchHandler != null) {
                onSearchHandler.run();
            }
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Write button
        Button writeBtn = createNavButton("✍️ Write", true);
        writeBtn.setOnAction(e -> {
            if (onWriteHandler != null) onWriteHandler.run();
        });
        
        // Notifications button with badge
        HBox notificationBox = new HBox();
        notificationBox.setAlignment(Pos.CENTER);
        Button notificationsBtn = createNavButton("🔔", false);
        notificationsBtn.setOnAction(e -> {
            if (onNotificationsHandler != null) onNotificationsHandler.run();
        });
        
        notificationBadge = new Label("3");
        notificationBadge.setStyle(
            "-fx-background-color: #ff4444; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 10px; " +
            "-fx-min-width: 20px; " +
            "-fx-min-height: 20px; " +
            "-fx-max-width: 20px; " +
            "-fx-max-height: 20px; " +
            "-fx-alignment: center; " +
            "-fx-font-size: 10px; " +
            "-fx-font-weight: bold;"
        );
        notificationBadge.setVisible(false);
        
        notificationBox.getChildren().addAll(notificationsBtn, notificationBadge);
        
        // User menu button
        Button userBtn = createNavButton("👤 " + username, false);
        userBtn.setOnAction(e -> {
            if (onProfileHandler != null) onProfileHandler.run();
        });
        
        // Logout button
        Button logoutBtn = createNavButton("Logout", false);
        logoutBtn.setOnAction(e -> {
            if (onLogoutHandler != null) onLogoutHandler.run();
        });
        
        this.getChildren().addAll(
            brandLabel,
            searchField,
            spacer,
            writeBtn,
            notificationBox,
            userBtn,
            logoutBtn
        );
    }
    
    private Button createNavButton(String text, boolean isPrimary) {
        Button btn = new Button(text);
        
        if (isPrimary) {
            btn.setStyle(
                "-fx-background-color: white; " +
                "-fx-text-fill: #667eea; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8px 20px; " +
                "-fx-background-radius: 20px; " +
                "-fx-cursor: hand;"
            );
            
            btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #f0f0f0; " +
                "-fx-text-fill: #667eea; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8px 20px; " +
                "-fx-background-radius: 20px; " +
                "-fx-cursor: hand;"
            ));
            
            btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: white; " +
                "-fx-text-fill: #667eea; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8px 20px; " +
                "-fx-background-radius: 20px; " +
                "-fx-cursor: hand;"
            ));
        } else {
            btn.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8px 16px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            );
            
            btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8px 16px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            ));
            
            btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8px 16px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            ));
        }
        
        return btn;
    }
    
    public void setNotificationCount(int count) {
        if (count > 0) {
            notificationBadge.setText(String.valueOf(count));
            notificationBadge.setVisible(true);
        } else {
            notificationBadge.setVisible(false);
        }
    }
    
    public String getSearchQuery() {
        return searchField.getText();
    }
    
    public void setOnSearch(Runnable handler) {
        this.onSearchHandler = handler;
    }
    
    public void setOnWrite(Runnable handler) {
        this.onWriteHandler = handler;
    }
    
    public void setOnNotifications(Runnable handler) {
        this.onNotificationsHandler = handler;
    }
    
    public void setOnProfile(Runnable handler) {
        this.onProfileHandler = handler;
    }
    
    public void setOnLogout(Runnable handler) {
        this.onLogoutHandler = handler;
    }
}
