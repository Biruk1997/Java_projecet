package com.blogapp.desktop.views;

import com.blogapp.desktop.services.AuthService;
import com.blogapp.desktop.utils.ErrorHandler;
import com.blogapp.desktop.utils.SessionManager;
import com.blogapp.desktop.utils.ValidationUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * LoginView - Enhanced login screen with API integration
 * Equivalent to React's Login.jsx
 */
public class LoginView extends VBox {
    
    private Stage stage;
    private TextField emailField;
    private PasswordField passwordField;
    private Label errorLabel;
    private Button loginButton;
    private ProgressIndicator progressIndicator;
    
    public LoginView(Stage stage) {
        this.stage = stage;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(40));
        this.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);");
        
        // Login Card
        VBox loginCard = new VBox(20);
        loginCard.setPadding(new Insets(40));
        loginCard.setMaxWidth(450);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);"
        );
        
        // Title
        Label titleLabel = new Label("📝 Blog Platform");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.web("#667eea"));
        
        Label subtitleLabel = new Label("Welcome Back!");
        subtitleLabel.setFont(Font.font("System", FontWeight.NORMAL, 18));
        subtitleLabel.setTextFill(Color.web("#666666"));
        
        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));
        
        // Email Field
        Label emailLabel = new Label("Email");
        emailLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 12px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px;"
        );
        
        // Password Field
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 12px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px;"
        );
        
        // Error Label
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(370);
        
        // Progress Indicator
        progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(30, 30);
        progressIndicator.setVisible(false);
        
        // Login Button
        loginButton = new Button("Sign In");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 14px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        
        loginButton.setOnAction(e -> handleLogin());
        
        // Enter key support
        passwordField.setOnAction(e -> handleLogin());
        
        // Signup Link
        Hyperlink signupLink = new Hyperlink("Don't have an account? Sign up here");
        signupLink.setStyle("-fx-text-fill: #667eea; -fx-font-size: 14px;");
        signupLink.setOnAction(e -> showSignupView());
        
        // Add all to card
        loginCard.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            separator,
            emailLabel,
            emailField,
            passwordLabel,
            passwordField,
            errorLabel,
            progressIndicator,
            loginButton,
            signupLink
        );
        
        this.getChildren().add(loginCard);
    }
    
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        // Clear previous error
        errorLabel.setVisible(false);
        
        // Validate
        ValidationUtils.ValidationResult emailValidation = ValidationUtils.validateEmail(email);
        if (!emailValidation.isValid) {
            showError(emailValidation.error);
            return;
        }
        
        if (password.isEmpty()) {
            showError("Password is required");
            return;
        }
        
        // Show loading
        setLoading(true);
        
        // Call API
        AuthService.login(email, password)
            .thenAccept(response -> {
                javafx.application.Platform.runLater(() -> {
                    // Save session
                    SessionManager.saveSession(
                        response.token,
                        response.user.getUserId(),
                        response.user.getUsername(),
                        response.user.getEmail()
                    );
                    
                    // Navigate to dashboard
                    showDashboard();
                });
            })
            .exceptionally(error -> {
                javafx.application.Platform.runLater(() -> {
                    setLoading(false);
                    showError("Invalid email or password");
                });
                return null;
            });
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void setLoading(boolean loading) {
        loginButton.setDisable(loading);
        progressIndicator.setVisible(loading);
        emailField.setDisable(loading);
        passwordField.setDisable(loading);
        
        if (loading) {
            loginButton.setText("Signing in...");
        } else {
            loginButton.setText("Sign In");
        }
    }
    
    private void showSignupView() {
        SignupView signupView = new SignupView(stage);
        Scene scene = new Scene(signupView, 800, 700);
        stage.setScene(scene);
        stage.setTitle("Sign Up - Blog Platform");
    }
    
    private void showDashboard() {
        DashboardView dashboardView = new DashboardView(stage);
        stage.setScene(dashboardView.createScene());
        stage.setTitle("Dashboard - Blog Platform");
    }
    
    public Scene createScene() {
        return new Scene(this, 800, 600);
    }
}
