package com.blogapp.desktop.views;

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
 * Settings View - Application settings
 */
public class SettingsView extends BorderPane {
    
    public SettingsView() {
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
        
        Label titleLabel = new Label("⚙️ Settings");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);
        
        Label subtitleLabel = new Label("Manage your account preferences");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.setTextFill(Color.web("#e0e0ff"));
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Account Section
        VBox accountSection = createAccountSection();
        
        // Appearance Section
        VBox appearanceSection = createAppearanceSection();
        
        // About Section
        VBox aboutSection = createAboutSection();
        
        contentBox.getChildren().addAll(header, accountSection, appearanceSection, aboutSection);
        
        scrollPane.setContent(centerWrapper);
        this.setCenter(scrollPane);
    }
    
    private VBox createAccountSection() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(30));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 20, 0, 0, 4);"
        );
        
        Label sectionTitle = new Label("Account");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.web("#1a1a1a"));
        
        // Logged in as
        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER_LEFT);
        Label userLabel = new Label("Logged in as:");
        userLabel.setFont(Font.font("System", 14));
        userLabel.setTextFill(Color.web("#666666"));
        
        Label usernameLabel = new Label(SessionManager.getUsername());
        usernameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        usernameLabel.setTextFill(Color.web("#1a1a1a"));
        
        userBox.getChildren().addAll(userLabel, usernameLabel);
        
        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(
            "-fx-background-color: #dc2626; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 24px; " +
            "-fx-background-radius: 6px; " +
            "-fx-cursor: hand;"
        );
        
        logoutButton.setOnMouseEntered(e -> {
            logoutButton.setStyle(
                "-fx-background-color: #b91c1c; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 24px; " +
                "-fx-background-radius: 6px; " +
                "-fx-cursor: hand;"
            );
        });
        
        logoutButton.setOnMouseExited(e -> {
            logoutButton.setStyle(
                "-fx-background-color: #dc2626; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 24px; " +
                "-fx-background-radius: 6px; " +
                "-fx-cursor: hand;"
            );
        });
        
        logoutButton.setOnAction(e -> handleLogout());
        
        section.getChildren().addAll(sectionTitle, userBox, logoutButton);
        return section;
    }
    
    private VBox createAppearanceSection() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(30));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 20, 0, 0, 4);"
        );
        
        Label sectionTitle = new Label("Appearance");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.web("#1a1a1a"));
        
        // Theme toggle (placeholder)
        HBox themeBox = new HBox(10);
        themeBox.setAlignment(Pos.CENTER_LEFT);
        
        Label themeLabel = new Label("Theme:");
        themeLabel.setFont(Font.font("System", 14));
        themeLabel.setTextFill(Color.web("#666666"));
        
        ToggleGroup themeGroup = new ToggleGroup();
        RadioButton lightRadio = new RadioButton("Light");
        lightRadio.setToggleGroup(themeGroup);
        lightRadio.setSelected(true);
        
        RadioButton darkRadio = new RadioButton("Dark");
        darkRadio.setToggleGroup(themeGroup);
        darkRadio.setDisable(true); // Not implemented yet
        
        themeBox.getChildren().addAll(themeLabel, lightRadio, darkRadio);
        
        section.getChildren().addAll(sectionTitle, themeBox);
        return section;
    }
    
    private VBox createAboutSection() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(30));
        section.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 20, 0, 0, 4);"
        );
        
        Label sectionTitle = new Label("About");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.web("#1a1a1a"));
        
        Label appName = new Label("Blog Platform Desktop");
        appName.setFont(Font.font("System", FontWeight.BOLD, 16));
        appName.setTextFill(Color.web("#1a1a1a"));
        
        Label version = new Label("Version 1.0.0");
        version.setFont(Font.font("System", 14));
        version.setTextFill(Color.web("#666666"));
        
        Label description = new Label("A modern blogging platform built with JavaFX and Spring Boot");
        description.setFont(Font.font("System", 14));
        description.setTextFill(Color.web("#666666"));
        description.setWrapText(true);
        
        section.getChildren().addAll(sectionTitle, appName, version, description);
        return section;
    }
    
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                SessionManager.clearSession();
                
                // Close settings window
                Stage stage = (Stage) this.getScene().getWindow();
                stage.close();
                
                // Show info
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Logged Out");
                info.setHeaderText(null);
                info.setContentText("You have been logged out successfully. Please restart the application to login again.");
                info.showAndWait();
            }
        });
    }
    
    public static void showInWindow() {
        Stage stage = new Stage();
        stage.setTitle("Settings");
        
        SettingsView view = new SettingsView();
        Scene scene = new Scene(view, 900, 700);
        stage.setScene(scene);
        stage.show();
    }
}
