package com.blogapp.desktop.components;

import com.blogapp.desktop.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * UserCard Component - Displays user profile card
 * Equivalent to React's UserCard.jsx
 */
public class UserCard extends VBox {
    
    private User user;
    private boolean showFollowButton;
    private Runnable onClickHandler;
    
    public UserCard(User user, boolean showFollowButton) {
        this.user = user;
        this.showFollowButton = showFollowButton;
        initializeUI();
    }
    
    public UserCard(User user, boolean showFollowButton, Runnable onClickHandler) {
        this.user = user;
        this.showFollowButton = showFollowButton;
        this.onClickHandler = onClickHandler;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setPadding(new Insets(20));
        this.setSpacing(12);
        this.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 12px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3); " +
            "-fx-cursor: hand;"
        );
        
        // Hover effect
        this.setOnMouseEntered(e -> {
            this.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 5); " +
                "-fx-cursor: hand;"
            );
        });
        
        this.setOnMouseExited(e -> {
            this.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3); " +
                "-fx-cursor: hand;"
            );
        });
        
        // Click handler
        if (onClickHandler != null) {
            this.setOnMouseClicked(e -> onClickHandler.run());
        }
        
        // Avatar
        Label avatarLabel = new Label(getInitials());
        avatarLabel.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 40px; " +
            "-fx-min-width: 80px; " +
            "-fx-min-height: 80px; " +
            "-fx-max-width: 80px; " +
            "-fx-max-height: 80px; " +
            "-fx-alignment: center; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 28px;"
        );
        avatarLabel.setAlignment(Pos.CENTER);
        
        HBox avatarBox = new HBox(avatarLabel);
        avatarBox.setAlignment(Pos.CENTER);
        this.getChildren().add(avatarBox);
        
        // Name
        Label nameLabel = new Label(user.getFirstname() + " " + user.getLastname());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.web("#1a1a1a"));
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        this.getChildren().add(nameLabel);
        
        // Username
        Label usernameLabel = new Label("@" + user.getUsername());
        usernameLabel.setFont(Font.font("System", 13));
        usernameLabel.setTextFill(Color.web("#666666"));
        usernameLabel.setAlignment(Pos.CENTER);
        usernameLabel.setMaxWidth(Double.MAX_VALUE);
        this.getChildren().add(usernameLabel);
        
        // Bio
        if (user.getBio() != null && !user.getBio().isEmpty()) {
            Label bioLabel = new Label(user.getBio());
            bioLabel.setFont(Font.font("System", 13));
            bioLabel.setTextFill(Color.web("#757575"));
            bioLabel.setWrapText(true);
            bioLabel.setAlignment(Pos.CENTER);
            bioLabel.setMaxWidth(250);
            this.getChildren().add(bioLabel);
        }
        
        // Stats
        HBox statsBox = createStatsBox();
        this.getChildren().add(statsBox);
        
        // Follow button
        if (showFollowButton) {
            FollowButton followBtn = new FollowButton(false);
            followBtn.setMaxWidth(Double.MAX_VALUE);
            this.getChildren().add(followBtn);
        }
    }
    
    private HBox createStatsBox() {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 0, 0));
        
        // Posts count
        VBox postsBox = new VBox(2);
        postsBox.setAlignment(Pos.CENTER);
        Label postsCount = new Label("0"); // TODO: Get from user stats
        postsCount.setFont(Font.font("System", FontWeight.BOLD, 16));
        postsCount.setTextFill(Color.web("#1a1a1a"));
        Label postsLabel = new Label("Posts");
        postsLabel.setFont(Font.font("System", 11));
        postsLabel.setTextFill(Color.web("#999999"));
        postsBox.getChildren().addAll(postsCount, postsLabel);
        
        // Followers count
        VBox followersBox = new VBox(2);
        followersBox.setAlignment(Pos.CENTER);
        Label followersCount = new Label("0"); // TODO: Get from user stats
        followersCount.setFont(Font.font("System", FontWeight.BOLD, 16));
        followersCount.setTextFill(Color.web("#1a1a1a"));
        Label followersLabel = new Label("Followers");
        followersLabel.setFont(Font.font("System", 11));
        followersLabel.setTextFill(Color.web("#999999"));
        followersBox.getChildren().addAll(followersCount, followersLabel);
        
        // Following count
        VBox followingBox = new VBox(2);
        followingBox.setAlignment(Pos.CENTER);
        Label followingCount = new Label("0"); // TODO: Get from user stats
        followingCount.setFont(Font.font("System", FontWeight.BOLD, 16));
        followingCount.setTextFill(Color.web("#1a1a1a"));
        Label followingLabel = new Label("Following");
        followingLabel.setFont(Font.font("System", 11));
        followingLabel.setTextFill(Color.web("#999999"));
        followingBox.getChildren().addAll(followingCount, followingLabel);
        
        box.getChildren().addAll(postsBox, followersBox, followingBox);
        return box;
    }
    
    private String getInitials() {
        String first = user.getFirstname() != null && !user.getFirstname().isEmpty() ? 
            user.getFirstname().substring(0, 1) : "";
        String last = user.getLastname() != null && !user.getLastname().isEmpty() ? 
            user.getLastname().substring(0, 1) : "";
        return (first + last).toUpperCase();
    }
    
    public User getUser() {
        return user;
    }
}
