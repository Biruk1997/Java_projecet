package com.blogapp.desktop.views;

import com.blogapp.desktop.models.User;
import com.blogapp.desktop.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Profile View - Shows user profile information
 */
public class ProfileView extends BorderPane {
    
    private User currentUser;
    
    public ProfileView(User user) {
        this.currentUser = user;
        initializeUI();
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
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-background-radius: 15px 15px 0 0;"
        );
        
        Label titleLabel = new Label("My Profile");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);
        
        Label subtitleLabel = new Label("Manage your personal information");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.setTextFill(Color.web("#e0e0ff"));
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Profile Card
        VBox profileCard = createProfileCard();
        
        // Stats Card
        VBox statsCard = createStatsCard();
        
        contentBox.getChildren().addAll(header, profileCard, statsCard);
        
        scrollPane.setContent(centerWrapper);
        this.setCenter(scrollPane);
    }
    
    private VBox createProfileCard() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(40));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 20, 0, 0, 4);"
        );
        
        // Avatar
        Label avatarLabel = new Label(getInitials());
        avatarLabel.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 75px; " +
            "-fx-min-width: 150px; " +
            "-fx-min-height: 150px; " +
            "-fx-max-width: 150px; " +
            "-fx-max-height: 150px; " +
            "-fx-alignment: center; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 56px; " +
            "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.4), 15, 0, 0, 5);"
        );
        
        HBox avatarBox = new HBox(avatarLabel);
        avatarBox.setAlignment(Pos.CENTER);
        
        // Name
        Label nameLabel = new Label(currentUser.getFullName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        nameLabel.setTextFill(Color.web("#1a1a1a"));
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        
        // Username
        Label usernameLabel = new Label("@" + currentUser.getUsername());
        usernameLabel.setFont(Font.font("System", 16));
        usernameLabel.setTextFill(Color.web("#666666"));
        usernameLabel.setAlignment(Pos.CENTER);
        usernameLabel.setMaxWidth(Double.MAX_VALUE);
        
        // Email
        HBox emailBox = createInfoRow("Email", currentUser.getEmail());
        
        // Bio
        if (currentUser.getBio() != null && !currentUser.getBio().isEmpty()) {
            VBox bioBox = new VBox(5);
            Label bioTitle = new Label("Bio");
            bioTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
            bioTitle.setTextFill(Color.web("#666666"));
            
            Label bioContent = new Label(currentUser.getBio());
            bioContent.setFont(Font.font("System", 14));
            bioContent.setTextFill(Color.web("#333333"));
            bioContent.setWrapText(true);
            
            bioBox.getChildren().addAll(bioTitle, bioContent);
            card.getChildren().addAll(avatarBox, nameLabel, usernameLabel, emailBox, bioBox);
        } else {
            card.getChildren().addAll(avatarBox, nameLabel, usernameLabel, emailBox);
        }
        
        return card;
    }
    
    private VBox createStatsCard() {
        VBox card = new VBox(25);
        card.setPadding(new Insets(40));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 20, 0, 0, 4);"
        );
        
        Label titleLabel = new Label("Statistics");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web("#1a1a1a"));
        
        // Stats grid
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(40);
        statsGrid.setVgap(20);
        
        // Posts
        VBox postsBox = createStatBox("Posts", String.valueOf(currentUser.getPostsCount()));
        statsGrid.add(postsBox, 0, 0);
        
        // Followers
        VBox followersBox = createStatBox("Followers", String.valueOf(currentUser.getFollowersCount()));
        statsGrid.add(followersBox, 1, 0);
        
        // Following
        VBox followingBox = createStatBox("Following", String.valueOf(currentUser.getFollowingCount()));
        statsGrid.add(followingBox, 2, 0);
        
        card.getChildren().addAll(titleLabel, statsGrid);
        return card;
    }
    
    private VBox createStatBox(String label, String value) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-cursor: hand; -fx-padding: 10px; -fx-background-radius: 10px;");
        
        // Hover effect
        box.setOnMouseEntered(e -> {
            box.setStyle("-fx-cursor: hand; -fx-padding: 10px; -fx-background-color: #f0f0ff; -fx-background-radius: 10px;");
        });
        
        box.setOnMouseExited(e -> {
            box.setStyle("-fx-cursor: hand; -fx-padding: 10px; -fx-background-radius: 10px;");
        });
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        valueLabel.setTextFill(Color.web("#667eea"));
        
        Label labelLabel = new Label(label);
        labelLabel.setFont(Font.font("System", 14));
        labelLabel.setTextFill(Color.web("#666666"));
        
        box.getChildren().addAll(valueLabel, labelLabel);
        
        // Add click handlers
        if (label.equals("Followers")) {
            box.setOnMouseClicked(e -> {
                FollowersView.showInWindow(currentUser.getUserId());
            });
        } else if (label.equals("Following")) {
            box.setOnMouseClicked(e -> {
                FollowingView.showInWindow(currentUser.getUserId());
            });
        }
        
        return box;
    }
    
    private HBox createInfoRow(String label, String value) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        
        Label labelLabel = new Label(label + ":");
        labelLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        labelLabel.setTextFill(Color.web("#666666"));
        labelLabel.setMinWidth(80);
        
        Label valueLabel = new Label(value != null ? value : "N/A");
        valueLabel.setFont(Font.font("System", 14));
        valueLabel.setTextFill(Color.web("#333333"));
        
        box.getChildren().addAll(labelLabel, valueLabel);
        return box;
    }
    
    private String getInitials() {
        if (currentUser.getFirstname() != null && !currentUser.getFirstname().isEmpty() && 
            currentUser.getLastname() != null && !currentUser.getLastname().isEmpty()) {
            return (currentUser.getFirstname().substring(0, 1) + currentUser.getLastname().substring(0, 1)).toUpperCase();
        }
        if (currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty()) {
            String[] parts = currentUser.getDisplayName().split(" ");
            if (parts.length >= 2) {
                return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
            }
            return currentUser.getDisplayName().substring(0, Math.min(2, currentUser.getDisplayName().length())).toUpperCase();
        }
        if (currentUser.getUsername() != null && !currentUser.getUsername().isEmpty()) {
            return currentUser.getUsername().substring(0, Math.min(2, currentUser.getUsername().length())).toUpperCase();
        }
        return "??";
    }
    
    public static void showInWindow(User user) {
        Stage stage = new Stage();
        stage.setTitle("My Profile");
        
        ProfileView view = new ProfileView(user);
        Scene scene = new Scene(view, 900, 700);
        stage.setScene(scene);
        stage.show();
    }
}
