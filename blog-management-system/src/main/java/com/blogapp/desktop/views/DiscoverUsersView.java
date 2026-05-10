package com.blogapp.desktop.views;

import com.blogapp.desktop.models.User;
import com.blogapp.desktop.services.FollowService;
import com.blogapp.desktop.services.UserService;
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
 * Discover Users View - Browse and follow other users
 */
public class DiscoverUsersView extends BorderPane {
    
    private VBox usersContainer;
    private ProgressIndicator loadingIndicator;
    private TextField searchField;
    
    public DiscoverUsersView() {
        initializeUI();
        loadAllUsers();
    }
    
    private void initializeUI() {
        this.setPadding(new Insets(0));
        this.setStyle("-fx-background-color: #f8f9fa;");
        
        // Main content
        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(40, 60, 40, 60));
        mainContent.setMaxWidth(1200);
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        // Header
        VBox header = createHeader();
        
        // Search bar
        HBox searchBar = createSearchBar();
        
        // Loading indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(40, 40);
        HBox loadingBox = new HBox(loadingIndicator);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setPadding(new Insets(40));
        
        // Users container
        usersContainer = new VBox(15);
        
        mainContent.getChildren().addAll(header, searchBar, loadingBox, usersContainer);
        
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
        
        Label titleLabel = new Label("🔍 Discover Writers");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.web("#1a1a1a"));
        
        Label subtitleLabel = new Label("Find interesting writers to follow and expand your network.");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.setTextFill(Color.web("#666666"));
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    private HBox createSearchBar() {
        HBox searchBar = new HBox(15);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(20));
        searchBar.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);"
        );
        
        Label searchIcon = new Label("🔍");
        searchIcon.setFont(Font.font("System", 18));
        
        searchField = new TextField();
        searchField.setPromptText("Search by name or username...");
        searchField.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-border-color: transparent; " +
            "-fx-font-size: 14px;"
        );
        searchField.setPrefWidth(400);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        Button searchButton = new Button("Search");
        searchButton.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 25px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        
        searchButton.setOnMouseEntered(e -> searchButton.setStyle(
            "-fx-background-color: linear-gradient(135deg, #5568d3 0%, #6a3f8f 100%); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 25px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        
        searchButton.setOnMouseExited(e -> searchButton.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 25px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        
        searchButton.setOnAction(e -> searchUsers());
        searchField.setOnAction(e -> searchUsers());
        
        Button showAllButton = new Button("Show All");
        showAllButton.setStyle(
            "-fx-background-color: #f0f0ff; " +
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        
        showAllButton.setOnAction(e -> {
            searchField.clear();
            loadAllUsers();
        });
        
        searchBar.getChildren().addAll(searchIcon, searchField, searchButton, showAllButton);
        return searchBar;
    }
    
    private void loadAllUsers() {
        loadingIndicator.setVisible(true);
        usersContainer.getChildren().clear();
        
        UserService.getAllUsers(SessionManager.getToken())
            .thenAccept(users -> {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    displayUsers(users);
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    showError("Failed to load users: " + error.getMessage());
                });
                return null;
            });
    }
    
    private void searchUsers() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadAllUsers();
            return;
        }
        
        loadingIndicator.setVisible(true);
        usersContainer.getChildren().clear();
        
        UserService.searchUsers(query, SessionManager.getToken())
            .thenAccept(users -> {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    displayUsers(users);
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    showError("Failed to search users: " + error.getMessage());
                });
                return null;
            });
    }
    
    private void displayUsers(List<User> users) {
        usersContainer.getChildren().clear();
        
        // Filter out current user
        String currentUserId = SessionManager.getUserId();
        List<User> filteredUsers = users.stream()
            .filter(u -> !u.getUserId().equals(currentUserId))
            .toList();
        
        if (filteredUsers.isEmpty()) {
            VBox emptyState = createEmptyState();
            usersContainer.getChildren().add(emptyState);
            return;
        }
        
        // Section header
        HBox sectionHeader = new HBox(10);
        sectionHeader.setAlignment(Pos.CENTER_LEFT);
        sectionHeader.setPadding(new Insets(10, 0, 10, 0));
        
        Label countLabel = new Label(filteredUsers.size() + " writers found");
        countLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        countLabel.setTextFill(Color.web("#667eea"));
        
        sectionHeader.getChildren().add(countLabel);
        usersContainer.getChildren().add(sectionHeader);
        
        // User cards
        for (User user : filteredUsers) {
            HBox userCard = createUserCard(user);
            usersContainer.getChildren().add(userCard);
        }
    }
    
    private HBox createUserCard(User user) {
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
        Label avatar = createAvatar(user);
        
        // User info
        VBox userInfo = createUserInfo(user);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Follow button with dynamic state
        Button followButton = new Button("Follow");
        followButton.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 25px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        
        // Check if already following
        FollowService.isFollowing(user.getUserId(), SessionManager.getToken())
            .thenAccept(isFollowing -> {
                Platform.runLater(() -> {
                    if (isFollowing) {
                        followButton.setText("Following");
                        followButton.setStyle(
                            "-fx-background-color: #f0f0ff; " +
                            "-fx-text-fill: #667eea; " +
                            "-fx-border-color: #667eea; " +
                            "-fx-border-width: 2px; " +
                            "-fx-font-size: 13px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 10px 25px; " +
                            "-fx-background-radius: 8px; " +
                            "-fx-cursor: hand;"
                        );
                    }
                });
            });
        
        followButton.setOnAction(e -> handleFollowToggle(user.getUserId(), followButton));
        
        card.getChildren().addAll(avatar, userInfo, spacer, followButton);
        return card;
    }
    
    private Label createAvatar(User user) {
        String initials = getInitials(user);
        Label avatar = new Label(initials);
        
        // Random gradient colors for variety
        String[] gradients = {
            "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
            "linear-gradient(135deg, #f093fb 0%, #f5576c 100%)",
            "linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)",
            "linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)",
            "linear-gradient(135deg, #fa709a 0%, #fee140 100%)",
            "linear-gradient(135deg, #30cfd0 0%, #330867 100%)",
            "linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)",
            "linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%)"
        };
        
        int index = Math.abs(user.getUserId().hashCode()) % gradients.length;
        
        avatar.setStyle(
            "-fx-background-color: " + gradients[index] + "; " +
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
        
        // Bio if available
        if (user.getBio() != null && !user.getBio().isEmpty()) {
            Label bioLabel = new Label(user.getBio());
            bioLabel.setFont(Font.font("System", 13));
            bioLabel.setTextFill(Color.web("#999999"));
            bioLabel.setWrapText(true);
            bioLabel.setMaxWidth(500);
            info.getChildren().addAll(nameLabel, usernameLabel, bioLabel);
        } else {
            info.getChildren().addAll(nameLabel, usernameLabel);
        }
        
        // Stats row
        HBox statsRow = new HBox(15);
        statsRow.setAlignment(Pos.CENTER_LEFT);
        
        Label followersLabel = new Label("👥 " + user.getFollowersCount() + " followers");
        followersLabel.setFont(Font.font("System", 12));
        followersLabel.setTextFill(Color.web("#999999"));
        
        Label postsLabel = new Label("📝 " + user.getPostsCount() + " posts");
        postsLabel.setFont(Font.font("System", 12));
        postsLabel.setTextFill(Color.web("#999999"));
        
        Label joinedLabel = new Label("📅 Joined " + formatDate(user.getCreatedAt()));
        joinedLabel.setFont(Font.font("System", 12));
        joinedLabel.setTextFill(Color.web("#999999"));
        
        statsRow.getChildren().addAll(followersLabel, new Label("•"), postsLabel, new Label("•"), joinedLabel);
        info.getChildren().add(statsRow);
        
        return info;
    }
    
    private void handleFollowToggle(String targetUserId, Button button) {
        String originalText = button.getText();
        button.setDisable(true);
        button.setText("Loading...");
        
        FollowService.toggleFollow(targetUserId, SessionManager.getToken())
            .thenAccept(isFollowing -> {
                Platform.runLater(() -> {
                    button.setDisable(false);
                    
                    if (isFollowing) {
                        button.setText("Following");
                        button.setStyle(
                            "-fx-background-color: #f0f0ff; " +
                            "-fx-text-fill: #667eea; " +
                            "-fx-border-color: #667eea; " +
                            "-fx-border-width: 2px; " +
                            "-fx-font-size: 13px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 10px 25px; " +
                            "-fx-background-radius: 8px; " +
                            "-fx-cursor: hand;"
                        );
                        
                        // Show success
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("You are now following this user!");
                        alert.show();
                        
                        new Thread(() -> {
                            try {
                                Thread.sleep(2000);
                                Platform.runLater(() -> alert.close());
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }).start();
                    } else {
                        button.setText("Follow");
                        button.setStyle(
                            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 13px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 10px 25px; " +
                            "-fx-background-radius: 8px; " +
                            "-fx-cursor: hand;"
                        );
                    }
                });
            })
            .exceptionally(error -> {
                Platform.runLater(() -> {
                    button.setDisable(false);
                    button.setText(originalText);
                    showError("Failed to toggle follow: " + error.getMessage());
                });
                return null;
            });
    }
    
    private VBox createEmptyState() {
        VBox emptyState = new VBox(15);
        emptyState.setAlignment(Pos.CENTER);
        emptyState.setPadding(new Insets(60));
        
        Label emojiLabel = new Label("🔍");
        emojiLabel.setFont(Font.font("System", 48));
        
        Label titleLabel = new Label("No users found");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#666666"));
        
        Label messageLabel = new Label("Try a different search term");
        messageLabel.setFont(Font.font("System", 14));
        messageLabel.setTextFill(Color.web("#999999"));
        
        emptyState.getChildren().addAll(emojiLabel, titleLabel, messageLabel);
        return emptyState;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        return dateTime.format(formatter);
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void showInWindow() {
        Stage stage = new Stage();
        stage.setTitle("Discover Writers");
        
        DiscoverUsersView view = new DiscoverUsersView();
        Scene scene = new Scene(view, 1000, 800);
        stage.setScene(scene);
        stage.show();
    }
}
