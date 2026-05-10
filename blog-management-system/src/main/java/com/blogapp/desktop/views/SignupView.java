package com.blogapp.desktop.views;

import com.blogapp.desktop.services.AuthService;
import com.blogapp.desktop.utils.ErrorHandler;
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
 * SignupView - Enhanced signup screen with validation
 * Equivalent to React's SignUp.jsx
 */
public class SignupView extends VBox {
    
    private Stage stage;
    private TextField firstnameField;
    private TextField lastnameField;
    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private TextArea bioField;
    private Label errorLabel;
    private Label passwordStrengthLabel;
    private Button signupButton;
    private ProgressIndicator progressIndicator;
    
    public SignupView(Stage stage) {
        this.stage = stage;
        initializeUI();
    }
    
    private void initializeUI() {
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(40));
        this.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);");
        
        // Signup Card
        VBox signupCard = new VBox(15);
        signupCard.setPadding(new Insets(40));
        signupCard.setMaxWidth(500);
        signupCard.setAlignment(Pos.CENTER);
        signupCard.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);"
        );
        
        // Title
        Label titleLabel = new Label("Create Account");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.web("#667eea"));
        
        Label subtitleLabel = new Label("Join our community of writers");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.setTextFill(Color.web("#666666"));
        
        Separator separator = new Separator();
        
        // Form Fields
        firstnameField = createTextField("First Name");
        lastnameField = createTextField("Last Name");
        usernameField = createTextField("Username");
        emailField = createTextField("Email");
        passwordField = createPasswordField("Password (min 8 characters)");
        bioField = createTextArea("Short bio (optional)");
        
        // Password strength indicator
        passwordStrengthLabel = new Label();
        passwordStrengthLabel.setFont(Font.font("System", 12));
        passwordStrengthLabel.setVisible(false);
        
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                passwordStrengthLabel.setVisible(false);
            } else {
                ValidationUtils.PasswordStrength strength = ValidationUtils.getPasswordStrength(newVal);
                passwordStrengthLabel.setText("Password Strength: " + strength.strength);
                passwordStrengthLabel.setTextFill(Color.web(strength.color));
                passwordStrengthLabel.setVisible(true);
            }
        });
        
        // Error Label
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(420);
        
        // Progress Indicator
        progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(30, 30);
        progressIndicator.setVisible(false);
        
        // Signup Button
        signupButton = new Button("Create Account");
        signupButton.setMaxWidth(Double.MAX_VALUE);
        signupButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 14px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        
        signupButton.setOnAction(e -> handleSignup());
        
        // Login Link
        Hyperlink loginLink = new Hyperlink("Already have an account? Sign in here");
        loginLink.setStyle("-fx-text-fill: #667eea; -fx-font-size: 14px;");
        loginLink.setOnAction(e -> showLoginView());
        
        // Add all to card
        signupCard.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            separator,
            createLabel("First Name"), firstnameField,
            createLabel("Last Name"), lastnameField,
            createLabel("Username"), usernameField,
            createLabel("Email"), emailField,
            createLabel("Password"), passwordField,
            passwordStrengthLabel,
            createLabel("Bio (Optional)"), bioField,
            errorLabel,
            progressIndicator,
            signupButton,
            loginLink
        );
        
        ScrollPane scrollPane = new ScrollPane(signupCard);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        this.getChildren().add(scrollPane);
    }
    
    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px;"
        );
        return field;
    }
    
    private PasswordField createPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px;"
        );
        return field;
    }
    
    private TextArea createTextArea(String prompt) {
        TextArea area = new TextArea();
        area.setPromptText(prompt);
        area.setPrefRowCount(3);
        area.setWrapText(true);
        area.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-radius: 8px;"
        );
        return area;
    }
    
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 13));
        return label;
    }
    
    private void handleSignup() {
        // Get values
        String firstname = firstnameField.getText().trim();
        String lastname = lastnameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String bio = bioField.getText().trim();
        
        // Clear error
        errorLabel.setVisible(false);
        
        // Validate
        ValidationUtils.ValidationResult firstnameValidation = ValidationUtils.validateName(firstname, "First name");
        if (!firstnameValidation.isValid) {
            showError(firstnameValidation.error);
            return;
        }
        
        ValidationUtils.ValidationResult lastnameValidation = ValidationUtils.validateName(lastname, "Last name");
        if (!lastnameValidation.isValid) {
            showError(lastnameValidation.error);
            return;
        }
        
        ValidationUtils.ValidationResult usernameValidation = ValidationUtils.validateUsername(username);
        if (!usernameValidation.isValid) {
            showError(usernameValidation.error);
            return;
        }
        
        ValidationUtils.ValidationResult emailValidation = ValidationUtils.validateEmail(email);
        if (!emailValidation.isValid) {
            showError(emailValidation.error);
            return;
        }
        
        ValidationUtils.ValidationResult passwordValidation = ValidationUtils.validatePassword(password);
        if (!passwordValidation.isValid) {
            showError(passwordValidation.error);
            return;
        }
        
        // Show loading
        setLoading(true);
        
        // Create request
        AuthService.SignupRequest request = new AuthService.SignupRequest();
        request.firstname = firstname;
        request.lastname = lastname;
        request.username = username;
        request.email = email;
        request.password = password;
        request.bio = bio.isEmpty() ? null : bio;
        
        // Call API
        AuthService.signup(request)
            .thenAccept(user -> {
                javafx.application.Platform.runLater(() -> {
                    ErrorHandler.showSuccess("Success", "Account created successfully! Please login.");
                    showLoginView();
                });
            })
            .exceptionally(error -> {
                javafx.application.Platform.runLater(() -> {
                    setLoading(false);
                    showError("Signup failed. Email or username may already exist.");
                });
                return null;
            });
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void setLoading(boolean loading) {
        signupButton.setDisable(loading);
        progressIndicator.setVisible(loading);
        firstnameField.setDisable(loading);
        lastnameField.setDisable(loading);
        usernameField.setDisable(loading);
        emailField.setDisable(loading);
        passwordField.setDisable(loading);
        bioField.setDisable(loading);
        
        if (loading) {
            signupButton.setText("Creating account...");
        } else {
            signupButton.setText("Create Account");
        }
    }
    
    private void showLoginView() {
        LoginView loginView = new LoginView(stage);
        stage.setScene(loginView.createScene());
        stage.setTitle("Login - Blog Platform");
    }
    
    public Scene createScene() {
        return new Scene(this, 800, 750);
    }
}
