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

import java.util.List;

/**
 * Following View - Shows list of users being followed
 */
public class FollowingView extends BorderPane {
    
    private String userId;
    private VBox followingContainer;
    private ProgressIndicator loadingIndicator;
    
    public FollowingView(String userId) {
        this.userId = userId;
        initializeUI();
        loadFollowing();
    }
    
    private void initializeUI() {
        this.setPadding(new Insets(0));
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);");
        
        // Scrollable content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        VBox contentBox = new VBox(30);
        contentBox.setPadding(new Insets(40, 60, 40, 60));
        contentBox.setMaxWidth(900);
        
        // Center wrapper
        HBox centerWrapper = new HBox(contentBox);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setStyle("-fx-background-color: transparent;");
        
        // Header with gradient background
        VBox header = new VBox(10);
        header.setPadding(new Insets(40, 40, 40, 40));
        header.setStyle(
            "-fx-background-color: linear-gradient(135deg, #10b981 0%, #059669 100%); " +
            "-fx-background-radius: 15px 15px 0 0;"
        );
        
        Label titleLabel = new Label("✨ Following");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);
        
        Label subtitleLabel = new Label("People this user follows");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.setTextFill(Color.web("#d1fae5"));
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Loading indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(50, 50);
        loadingIndicator.setVisible(true);
        
        HBox loadingBox = new HBox(loadingIndicator);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setPadding(new Insets(60));
        
        // Following container
        followingContainer = new VBox(20);
        followingContainer.setMaxWidth(900);
        
        contentBox.getChildren().addAll(header, loadingBox, followingContainer);
        
        scrollPane.setContent(centerWrapper);
        this.setCenter(scrollPane);
    }
    
    private void loadFollowing() {
        FollowService.getFollowing(userId, SessionManager.getToken())
            .thenAccept(following -> {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    displayFollowing(following);
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    showError("Failed to load following: " + error.getMessage());
                });
                return null;
            });
    }
    
    private void displayFollowing(List<User> following) {
        followingContainer.getChildren().clear();
        
        if (following.isEmpty()) {
            Label emptyLabel = new Label("Not following anyone yet");
            emptyLabel.setFont(Font.font("System", 16));
            emptyLabel.setTextFill(Color.web("#999999"));
            emptyLabel.setPadding(new Insets(40));
            
            HBox emptyBox = new HBox(emptyLabel);
            emptyBox.setAlignment(Pos.CENTER);
            followingContainer.getChildren().add(emptyBox);
            return;
        }
        
        for (User user : following) {
            HBox userCard = createUserCard(user);
            followingContainer.getChildren().add(userCard);
        }
    }
    
    private HBox createUserCard(User user) {
        HBox card = new HBox(20);
        card.setPadding(new Insets(25));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 18, 0, 0, 4); " +
            "-fx-cursor: hand;"
        );
        
        // Hover effect
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 15px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 25, 0, 0, 6); " +
                "-fx-cursor: hand;"
            );
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 15px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 18, 0, 0, 4); " +
                "-fx-cursor: hand;"
            );
        });
        
        // Avatar
        String initials = getInitials(user);
        Label avatarLabel = new Label(initials);
        avatarLabel.setStyle(
            "-fx-background-color: linear-gradient(135deg, #10b981 0%, #059669 100%); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 35px; " +
            "-fx-min-width: 70px; " +
            "-fx-min-height: 70px; " +
            "-fx-max-width: 70px; " +
            "-fx-max-height: 70px; " +
            "-fx-alignment: center; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 24px; " +
            "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.3), 10, 0, 0, 3);"
        );
        
        // User info
        VBox userInfo = new VBox(8);
        Label nameLabel = new Label(user.getFullName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.web("#1a1a1a"));
        
        Label usernameLabel = new Label("@" + user.getUsername());
        usernameLabel.setFont(Font.font("System", 14));
        usernameLabel.setTextFill(Color.web("#666666"));
        
        if (user.getBio() != null && !user.getBio().isEmpty()) {
            Label bioLabel = new Label(user.getBio());
            bioLabel.setFont(Font.font("System", 13));
            bioLabel.setTextFill(Color.web("#999999"));
            bioLabel.setWrapText(true);
            bioLabel.setMaxWidth(400);
            userInfo.getChildren().addAll(nameLabel, usernameLabel, bioLabel);
        } else {
            userInfo.getChildren().addAll(nameLabel, usernameLabel);
        }
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Follow button (if not current user)
        if (!user.getUserId().equals(SessionManager.getUserId())) {
            // Create follow button - since they're in following list, we're following them
            FollowButton followButton = new FollowButton(true);
            
            // Set click handler
            followButton.setOnAction(e -> handleFollowToggle(user.getUserId(), followButton));
            
            card.getChildren().addAll(avatarLabel, userInfo, spacer, followButton);
        } else {
            Label youLabel = new Label("You");
            youLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            youLabel.setTextFill(Color.web("#10b981"));
            card.getChildren().addAll(avatarLabel, userInfo, spacer, youLabel);
        }
        
        return card;
    }
    
    private void handleFollowToggle(String targetUserId, FollowButton button) {
        button.setLoading(true);
        
        FollowService.toggleFollow(targetUserId, SessionManager.getToken())
            .thenAccept(isFollowing -> {
                Platform.runLater(() -> {
                    button.setLoading(false);
                    button.setFollowing(isFollowing);
                    System.out.println("Follow toggled successfully. Now following: " + isFollowing);
                    
                    // Show success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText(isFollowing ? "You are now following this user!" : "You unfollowed this user.");
                    alert.show();
                    
                    // Auto-close after 2 seconds
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                            Platform.runLater(() -> {
                                alert.close();
                                // Reload the list if unfollowed
                                if (!isFollowing) {
                                    loadFollowing();
                                }
                            });
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    button.setLoading(false);
                    showError("Failed to toggle follow: " + error.getMessage());
                    error.printStackTrace();
                });
                return null;
            });
    }
    
    private String getInitials(User user) {
        if (user.getFirstname() != null && !user.getFirstname().isEmpty() && 
            user.getLastname() != null && !user.getLastname().isEmpty()) {
            return (user.getFirstname().substring(0, 1) + user.getLastname().substring(0, 1)).toUpperCase();
        }
        if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            String[] parts = user.getDisplayName().split(" ");
            if (parts.length >= 2) {
                return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
            }
            return user.getDisplayName().substring(0, Math.min(2, user.getDisplayName().length())).toUpperCase();
        }
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            return user.getUsername().substring(0, Math.min(2, user.getUsername().length())).toUpperCase();
        }
        return "??";
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
        stage.setTitle("Following");
        
        FollowingView view = new FollowingView(userId);
        Scene scene = new Scene(view, 900, 700);
        stage.setScene(scene);
        stage.show();
    }
}
