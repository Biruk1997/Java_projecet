package com.blogapp.desktop.views;

import com.blogapp.desktop.components.FollowButton;
import com.blogapp.desktop.models.User;
import com.blogapp.desktop.services.FollowService;
import com.blogapp.desktop.utils.SessionManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Follow Management View - Combined Followers & Following with tabs
 * Modern dashboard-style interface
 */
public class FollowManagementView extends BorderPane {
    
    private String userId;
    private VBox followersContainer;
    private VBox followingContainer;
    private ProgressIndicator followersLoading;
    private ProgressIndicator followingLoading;
    private Label followersCountLabel;
    private Label followingCountLabel;
    private int followersCount = 0;
    private int followingCount = 0;
    
    public FollowManagementView(String userId) {
        this.userId = userId;
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        this.setPadding(new Insets(0));
        this.setStyle("-fx-background-color: #f8f9fa;");
        
        // Main content container
        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(40, 60, 40, 60));
        mainContent.setMaxWidth(1200);
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        // Header Section
        VBox header = createHeader();
        
        // Tabs Section
        TabPane tabPane = createTabPane();
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        
        // Tips Section
        VBox tipsSection = createTipsSection();
        
        mainContent.getChildren().addAll(header, tabPane, tipsSection);
        
        // Center wrapper
        HBox centerWrapper = new HBox(mainContent);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setStyle("-fx-background-color: #f8f9fa;");
        
        ScrollPane scrollPane = new ScrollPane(centerWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f8f9fa; -fx-background-color: #f8f9fa;");
        
        this.setCenter(scrollPane);
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("Manage Followers & Following");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.web("#1a1a1a"));
        
        Label subtitleLabel = new Label("Connect with other writers, follow their work, and build your network.");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.setTextFill(Color.web("#666666"));
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-radius: 15px; " +
            "-fx-background-radius: 15px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);"
        );
        
        // Followers Tab
        Tab followersTab = new Tab();
        followersCountLabel = new Label("Followers (0)");
        followersCountLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        followersCountLabel.setTextFill(Color.web("#667eea"));
        followersTab.setGraphic(followersCountLabel);
        followersTab.setContent(createFollowersContent());
        
        // Following Tab
        Tab followingTab = new Tab();
        followingCountLabel = new Label("Following (0)");
        followingCountLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        followingCountLabel.setTextFill(Color.web("#667eea"));
        followingTab.setGraphic(followingCountLabel);
        followingTab.setContent(createFollowingContent());
        
        tabPane.getTabs().addAll(followersTab, followingTab);
        
        return tabPane;
    }
    
    private VBox createFollowersContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        
        // Section header
        VBox sectionHeader = new VBox(5);
        Label titleLabel = new Label("Your Followers");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web("#1a1a1a"));
        
        Label descLabel = new Label("People who follow your stories");
        descLabel.setFont(Font.font("System", 14));
        descLabel.setTextFill(Color.web("#999999"));
        
        sectionHeader.getChildren().addAll(titleLabel, descLabel);
        
        // Loading indicator
        followersLoading = new ProgressIndicator();
        followersLoading.setMaxSize(40, 40);
        HBox loadingBox = new HBox(followersLoading);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setPadding(new Insets(40));
        
        // Followers container
        followersContainer = new VBox(15);
        
        content.getChildren().addAll(sectionHeader, loadingBox, followersContainer);
        
        return content;
    }
    
    private VBox createFollowingContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        
        // Section header
        VBox sectionHeader = new VBox(5);
        Label titleLabel = new Label("You're Following");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web("#1a1a1a"));
        
        Label descLabel = new Label("Writers whose stories you follow");
        descLabel.setFont(Font.font("System", 14));
        descLabel.setTextFill(Color.web("#999999"));
        
        sectionHeader.getChildren().addAll(titleLabel, descLabel);
        
        // Loading indicator
        followingLoading = new ProgressIndicator();
        followingLoading.setMaxSize(40, 40);
        HBox loadingBox = new HBox(followingLoading);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setPadding(new Insets(40));
        
        // Following container
        followingContainer = new VBox(15);
        
        content.getChildren().addAll(sectionHeader, loadingBox, followingContainer);
        
        return content;
    }
    
    private VBox createTipsSection() {
        VBox tipsBox = new VBox(15);
        tipsBox.setPadding(new Insets(25));
        tipsBox.setStyle(
            "-fx-background-color: linear-gradient(135deg, #f0f0ff 0%, #e8e8ff 100%); " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #d0d0ff; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px;"
        );
        
        Label tipsTitle = new Label("💡 Tips");
        tipsTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        tipsTitle.setTextFill(Color.web("#667eea"));
        
        VBox tipsList = new VBox(8);
        tipsList.getChildren().addAll(
            createTipLabel("• Follow writers whose content you enjoy"),
            createTipLabel("• Engage with posts and leave comments"),
            createTipLabel("• You can unfollow anytime"),
            createTipLabel("• They'll be notified when you follow them")
        );
        
        tipsBox.getChildren().addAll(tipsTitle, tipsList);
        return tipsBox;
    }
    
    private Label createTipLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", 14));
        label.setTextFill(Color.web("#555555"));
        label.setWrapText(true);
        return label;
    }
    
    private void loadData() {
        loadFollowers();
        loadFollowing();
    }
    
    private void loadFollowers() {
        FollowService.getFollowers(userId, SessionManager.getToken())
            .thenAccept(followers -> {
                Platform.runLater(() -> {
                    followersLoading.setVisible(false);
                    followersCount = followers.size();
                    followersCountLabel.setText("Followers (" + followersCount + ")");
                    displayFollowers(followers);
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    followersLoading.setVisible(false);
                    showError("Failed to load followers: " + error.getMessage());
                });
                return null;
            });
    }
    
    private void loadFollowing() {
        FollowService.getFollowing(userId, SessionManager.getToken())
            .thenAccept(following -> {
                Platform.runLater(() -> {
                    followingLoading.setVisible(false);
                    followingCount = following.size();
                    followingCountLabel.setText("Following (" + followingCount + ")");
                    displayFollowing(following);
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    followingLoading.setVisible(false);
                    showError("Failed to load following: " + error.getMessage());
                });
                return null;
            });
    }
    
    private void displayFollowers(List<User> followers) {
        followersContainer.getChildren().clear();
        
        if (followers.isEmpty()) {
            VBox emptyState = createEmptyState(
                "👥",
                "No followers yet",
                "Share your stories to attract followers!"
            );
            followersContainer.getChildren().add(emptyState);
            return;
        }
        
        for (User follower : followers) {
            HBox userCard = createFollowerCard(follower);
            followersContainer.getChildren().add(userCard);
        }
    }
    
    private void displayFollowing(List<User> following) {
        followingContainer.getChildren().clear();
        
        if (following.isEmpty()) {
            VBox emptyState = createEmptyState(
                "✨",
                "Not following anyone yet",
                "Discover writers and follow their stories!"
            );
            followingContainer.getChildren().add(emptyState);
            return;
        }
        
        for (User user : following) {
            HBox userCard = createFollowingCard(user);
            followingContainer.getChildren().add(userCard);
        }
    }
    
    private HBox createFollowerCard(User user) {
        HBox card = new HBox(20);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);"
        );
        
        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: #fafafa; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #667eea; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px; " +
            "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.15), 12, 0, 0, 3); " +
            "-fx-cursor: hand;"
        ));
        
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);"
        ));
        
        // Avatar
        Label avatar = createAvatar(user, "#667eea", "#764ba2");
        
        // User info
        VBox userInfo = createUserInfo(user);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // View Profile button
        Button viewProfileBtn = new Button("View Profile");
        viewProfileBtn.setStyle(
            "-fx-background-color: #f0f0ff; " +
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        
        viewProfileBtn.setOnMouseEntered(e -> viewProfileBtn.setStyle(
            "-fx-background-color: #667eea; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        
        viewProfileBtn.setOnMouseExited(e -> viewProfileBtn.setStyle(
            "-fx-background-color: #f0f0ff; " +
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        
        card.getChildren().addAll(avatar, userInfo, spacer, viewProfileBtn);
        return card;
    }
    
    private HBox createFollowingCard(User user) {
        HBox card = new HBox(20);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);"
        );
        
        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: #fafafa; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #667eea; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px; " +
            "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.15), 12, 0, 0, 3); " +
            "-fx-cursor: hand;"
        ));
        
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);"
        ));
        
        // Avatar
        Label avatar = createAvatar(user, "#10b981", "#059669");
        
        // User info
        VBox userInfo = createUserInfo(user);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Unfollow button
        Button unfollowBtn = new Button("Unfollow");
        unfollowBtn.setStyle(
            "-fx-background-color: #ffebee; " +
            "-fx-text-fill: #d32f2f; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        
        unfollowBtn.setOnMouseEntered(e -> unfollowBtn.setStyle(
            "-fx-background-color: #d32f2f; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        
        unfollowBtn.setOnMouseExited(e -> unfollowBtn.setStyle(
            "-fx-background-color: #ffebee; " +
            "-fx-text-fill: #d32f2f; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        
        unfollowBtn.setOnAction(e -> handleUnfollow(user.getUserId(), unfollowBtn));
        
        card.getChildren().addAll(avatar, userInfo, spacer, unfollowBtn);
        return card;
    }
    
    private Label createAvatar(User user, String color1, String color2) {
        String initials = getInitials(user);
        Label avatar = new Label(initials);
        avatar.setStyle(
            "-fx-background-color: linear-gradient(135deg, " + color1 + " 0%, " + color2 + " 100%); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 30px; " +
            "-fx-min-width: 60px; " +
            "-fx-min-height: 60px; " +
            "-fx-max-width: 60px; " +
            "-fx-max-height: 60px; " +
            "-fx-alignment: center; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 20px;"
        );
        return avatar;
    }
    
    private VBox createUserInfo(User user) {
        VBox info = new VBox(5);
        
        Label nameLabel = new Label(user.getFullName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#1a1a1a"));
        
        Label usernameLabel = new Label("@" + user.getUsername());
        usernameLabel.setFont(Font.font("System", 13));
        usernameLabel.setTextFill(Color.web("#666666"));
        
        // Stats row
        HBox statsRow = new HBox(15);
        statsRow.setAlignment(Pos.CENTER_LEFT);
        
        Label followersLabel = new Label(user.getFollowersCount() + " followers");
        followersLabel.setFont(Font.font("System", 12));
        followersLabel.setTextFill(Color.web("#999999"));
        
        Label joinedLabel = new Label("Joined " + formatDate(user.getCreatedAt()));
        joinedLabel.setFont(Font.font("System", 12));
        joinedLabel.setTextFill(Color.web("#999999"));
        
        statsRow.getChildren().addAll(followersLabel, new Label("•"), joinedLabel);
        
        info.getChildren().addAll(nameLabel, usernameLabel, statsRow);
        return info;
    }
    
    private VBox createEmptyState(String emoji, String title, String message) {
        VBox emptyState = new VBox(15);
        emptyState.setAlignment(Pos.CENTER);
        emptyState.setPadding(new Insets(60));
        
        Label emojiLabel = new Label(emoji);
        emojiLabel.setFont(Font.font("System", 48));
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#666666"));
        
        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("System", 14));
        messageLabel.setTextFill(Color.web("#999999"));
        
        emptyState.getChildren().addAll(emojiLabel, titleLabel, messageLabel);
        return emptyState;
    }
    
    private void handleUnfollow(String targetUserId, Button button) {
        button.setDisable(true);
        button.setText("Unfollowing...");
        
        FollowService.toggleFollow(targetUserId, SessionManager.getToken())
            .thenAccept(isFollowing -> {
                Platform.runLater(() -> {
                    button.setDisable(false);
                    button.setText("Unfollow");
                    
                    // Show success and reload
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("You unfollowed this user.");
                    alert.show();
                    
                    new Thread(() -> {
                        try {
                            Thread.sleep(1500);
                            Platform.runLater(() -> {
                                alert.close();
                                loadFollowing();
                            });
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    button.setDisable(false);
                    button.setText("Unfollow");
                    showError("Failed to unfollow: " + error.getMessage());
                });
                return null;
            });
    }
    
    private String getInitials(User user) {
        if (user.getFirstname() != null && !user.getFirstname().isEmpty() && 
            user.getLastname() != null && !user.getLastname().isEmpty()) {
            return (user.getFirstname().substring(0, 1) + user.getLastname().substring(0, 1)).toUpperCase();
        }
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            return user.getUsername().substring(0, Math.min(2, user.getUsername().length())).toUpperCase();
        }
        return "??";
    }
    
    private String formatDate(java.time.LocalDateTime dateTime) {
        if (dateTime == null) return "Recently";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return dateTime.format(formatter);
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void showInWindow(String userId) {
        Stage stage = new Stage();
        stage.setTitle("Manage Followers & Following");
        
        FollowManagementView view = new FollowManagementView(userId);
        Scene scene = new Scene(view, 1000, 800);
        stage.setScene(scene);
        stage.show();
    }
}
