package com.blogapp.desktop.views;

import com.blogapp.desktop.models.Topic;
import com.blogapp.desktop.components.TopicChip;
import com.blogapp.desktop.services.PostService;
import com.blogapp.desktop.services.TopicService;
import com.blogapp.desktop.utils.ErrorHandler;
import com.blogapp.desktop.utils.HtmlParser;
import com.blogapp.desktop.utils.ReadingTimeCalculator;
import com.blogapp.desktop.utils.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CreatePostView - Create/edit posts with rich text editor
 * Equivalent to React's NewStory.jsx
 */
public class CreatePostView extends BorderPane {
    
    private Stage stage;
    private String token;
    private TextField titleField;
    private TextField subtitleField;
    private HTMLEditor contentEditor;
    private TextField coverImageField;
    private FlowPane selectedTopicsPane;
    private FlowPane availableTopicsPane;
    private List<Topic> selectedTopics = new ArrayList<>();
    private List<Topic> availableTopics = new ArrayList<>();
    private Label wordCountLabel;
    private Label readingTimeLabel;
    private ProgressIndicator progressIndicator;
    
    public CreatePostView(Stage stage) {
        this.stage = stage;
        this.token = SessionManager.getToken();
        initializeUI();
        loadTopics();
    }
    
    private void initializeUI() {
        // Set background gradient
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);");
        
        // Top Bar
        HBox topBar = createTopBar();
        this.setTop(topBar);
        
        // Main Content with centered layout
        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(50, 80, 50, 80));
        mainContent.setMaxWidth(1000);
        mainContent.setStyle("-fx-alignment: center;");
        
        // Center wrapper
        HBox centerWrapper = new HBox(mainContent);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setStyle("-fx-background-color: transparent;");
        
        // Editor Card
        VBox editorCard = new VBox(25);
        editorCard.setPadding(new Insets(50));
        editorCard.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 20px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 25, 0, 0, 5);"
        );
        
        // Cover Image Section
        VBox coverSection = createCoverImageSection();
        editorCard.getChildren().add(coverSection);
        
        // Title Field
        titleField = new TextField();
        titleField.setPromptText("✍️ Your story title...");
        titleField.setStyle(
            "-fx-font-size: 36px; " +
            "-fx-font-weight: bold; " +
            "-fx-border-width: 0 0 2px 0; " +
            "-fx-border-color: transparent; " +
            "-fx-background-color: transparent; " +
            "-fx-prompt-text-fill: #cccccc; " +
            "-fx-text-fill: #1a1a1a; " +
            "-fx-padding: 10px 0;"
        );
        
        // Focus effect for title
        titleField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                titleField.setStyle(
                    "-fx-font-size: 36px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-border-width: 0 0 2px 0; " +
                    "-fx-border-color: #667eea; " +
                    "-fx-background-color: transparent; " +
                    "-fx-prompt-text-fill: #cccccc; " +
                    "-fx-text-fill: #1a1a1a; " +
                    "-fx-padding: 10px 0;"
                );
            } else {
                titleField.setStyle(
                    "-fx-font-size: 36px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-border-width: 0 0 2px 0; " +
                    "-fx-border-color: transparent; " +
                    "-fx-background-color: transparent; " +
                    "-fx-prompt-text-fill: #cccccc; " +
                    "-fx-text-fill: #1a1a1a; " +
                    "-fx-padding: 10px 0;"
                );
            }
        });
        
        editorCard.getChildren().add(titleField);
        
        // Subtitle Field
        subtitleField = new TextField();
        subtitleField.setPromptText("📝 Add a compelling subtitle...");
        subtitleField.setStyle(
            "-fx-font-size: 20px; " +
            "-fx-border-width: 0 0 1px 0; " +
            "-fx-border-color: transparent; " +
            "-fx-background-color: transparent; " +
            "-fx-prompt-text-fill: #cccccc; " +
            "-fx-text-fill: #666666; " +
            "-fx-padding: 8px 0;"
        );
        
        // Focus effect for subtitle
        subtitleField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                subtitleField.setStyle(
                    "-fx-font-size: 20px; " +
                    "-fx-border-width: 0 0 1px 0; " +
                    "-fx-border-color: #667eea; " +
                    "-fx-background-color: transparent; " +
                    "-fx-prompt-text-fill: #cccccc; " +
                    "-fx-text-fill: #666666; " +
                    "-fx-padding: 8px 0;"
                );
            } else {
                subtitleField.setStyle(
                    "-fx-font-size: 20px; " +
                    "-fx-border-width: 0 0 1px 0; " +
                    "-fx-border-color: transparent; " +
                    "-fx-background-color: transparent; " +
                    "-fx-prompt-text-fill: #cccccc; " +
                    "-fx-text-fill: #666666; " +
                    "-fx-padding: 8px 0;"
                );
            }
        });
        
        editorCard.getChildren().add(subtitleField);
        
        // Separator
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #e0e0e0;");
        editorCard.getChildren().add(separator);
        
        // Content Editor
        contentEditor = new HTMLEditor();
        contentEditor.setPrefHeight(500);
        contentEditor.setHtmlText("<p style='font-size: 18px; color: #333; line-height: 1.8;'>Start writing your amazing story...</p>");
        contentEditor.setStyle(
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 12px; " +
            "-fx-background-radius: 12px;"
        );
        editorCard.getChildren().add(contentEditor);
        
        // Writing Stats
        HBox statsBox = createStatsBox();
        editorCard.getChildren().add(statsBox);
        
        mainContent.getChildren().add(editorCard);
        
        // Topics Section (separate card)
        VBox topicsCard = createTopicsSection();
        mainContent.getChildren().add(topicsCard);
        
        ScrollPane scrollPane = new ScrollPane(centerWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        this.setCenter(scrollPane);
        
        // Update stats on content change
        contentEditor.setOnKeyReleased(e -> updateStats());
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(18, 30, 18, 30));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 3);"
        );
        
        Button backButton = new Button("← Back to Dashboard");
        backButton.setStyle(
            "-fx-background-color: rgba(255,255,255,0.2); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        
        backButton.setOnMouseEntered(e -> {
            backButton.setStyle(
                "-fx-background-color: rgba(255,255,255,0.3); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 20px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            );
        });
        
        backButton.setOnMouseExited(e -> {
            backButton.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 20px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            );
        });
        
        backButton.setOnAction(e -> goBack());
        
        Label titleLabel = new Label("✍️ Write Your Story");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.WHITE);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(24, 24);
        progressIndicator.setVisible(false);
        progressIndicator.setStyle("-fx-progress-color: white;");
        
        Button saveDraftButton = new Button("💾 Save Draft");
        saveDraftButton.setStyle(
            "-fx-background-color: rgba(255,255,255,0.95); " +
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 24px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        
        saveDraftButton.setOnMouseEntered(e -> {
            saveDraftButton.setStyle(
                "-fx-background-color: white; " +
                "-fx-text-fill: #667eea; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 24px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);"
            );
        });
        
        saveDraftButton.setOnMouseExited(e -> {
            saveDraftButton.setStyle(
                "-fx-background-color: rgba(255,255,255,0.95); " +
                "-fx-text-fill: #667eea; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 24px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            );
        });
        
        saveDraftButton.setOnAction(e -> saveDraft());
        
        Button publishButton = new Button("🚀 Publish Story");
        publishButton.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 28px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 3);"
        );
        
        publishButton.setOnMouseEntered(e -> {
            publishButton.setStyle(
                "-fx-background-color: #f0f0ff; " +
                "-fx-text-fill: #667eea; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 28px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 15, 0, 0, 4);"
            );
        });
        
        publishButton.setOnMouseExited(e -> {
            publishButton.setStyle(
                "-fx-background-color: white; " +
                "-fx-text-fill: #667eea; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 28px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 3);"
            );
        });
        
        publishButton.setOnAction(e -> publish());
        
        topBar.getChildren().addAll(
            backButton,
            titleLabel,
            spacer,
            progressIndicator,
            saveDraftButton,
            publishButton
        );
        
        return topBar;
    }
    
    private VBox createCoverImageSection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(20));
        section.setStyle(
            "-fx-background-color: #f8f9fa; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px;"
        );
        
        Label label = new Label("🖼️ Cover Image");
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        label.setTextFill(Color.web("#1a1a1a"));
        
        Label helpText = new Label("Add a visually appealing cover image to attract readers");
        helpText.setFont(Font.font("System", 12));
        helpText.setTextFill(Color.web("#666666"));
        
        coverImageField = new TextField();
        coverImageField.setPromptText("https://example.com/your-image.jpg");
        coverImageField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 12px; " +
            "-fx-border-color: #d0d0d0; " +
            "-fx-border-radius: 8px; " +
            "-fx-background-color: white; " +
            "-fx-background-radius: 8px;"
        );
        
        // Focus effect
        coverImageField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                coverImageField.setStyle(
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 12px; " +
                    "-fx-border-color: #667eea; " +
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 8px; " +
                    "-fx-background-color: white; " +
                    "-fx-background-radius: 8px;"
                );
            } else {
                coverImageField.setStyle(
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 12px; " +
                    "-fx-border-color: #d0d0d0; " +
                    "-fx-border-radius: 8px; " +
                    "-fx-background-color: white; " +
                    "-fx-background-radius: 8px;"
                );
            }
        });
        
        section.getChildren().addAll(label, helpText, coverImageField);
        return section;
    }
    
    private HBox createStatsBox() {
        HBox statsBox = new HBox(40);
        statsBox.setPadding(new Insets(20));
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setStyle(
            "-fx-background-color: linear-gradient(135deg, #f0f0ff 0%, #e8f0fe 100%); " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #d0d0ff; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 12px;"
        );
        
        VBox wordsBox = new VBox(5);
        wordsBox.setAlignment(Pos.CENTER);
        
        Label wordsIcon = new Label("📊");
        wordsIcon.setFont(Font.font("System", 24));
        
        wordCountLabel = new Label("0 words");
        wordCountLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        wordCountLabel.setTextFill(Color.web("#667eea"));
        
        Label wordsSubLabel = new Label("Word Count");
        wordsSubLabel.setFont(Font.font("System", 11));
        wordsSubLabel.setTextFill(Color.web("#666666"));
        
        wordsBox.getChildren().addAll(wordsIcon, wordCountLabel, wordsSubLabel);
        
        // Separator
        Separator verticalSep = new Separator();
        verticalSep.setOrientation(javafx.geometry.Orientation.VERTICAL);
        verticalSep.setPrefHeight(60);
        verticalSep.setStyle("-fx-background-color: #d0d0ff;");
        
        VBox timeBox = new VBox(5);
        timeBox.setAlignment(Pos.CENTER);
        
        Label timeIcon = new Label("⏱️");
        timeIcon.setFont(Font.font("System", 24));
        
        readingTimeLabel = new Label("1 min");
        readingTimeLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        readingTimeLabel.setTextFill(Color.web("#667eea"));
        
        Label timeSubLabel = new Label("Reading Time");
        timeSubLabel.setFont(Font.font("System", 11));
        timeSubLabel.setTextFill(Color.web("#666666"));
        
        timeBox.getChildren().addAll(timeIcon, readingTimeLabel, timeSubLabel);
        
        statsBox.getChildren().addAll(wordsBox, verticalSep, timeBox);
        return statsBox;
    }
    
    private VBox createTopicsSection() {
        VBox section = new VBox(15);
        
        Label label = new Label("Topics (up to 5)");
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        // Selected topics
        Label selectedLabel = new Label("Selected:");
        selectedLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        
        selectedTopicsPane = new FlowPane(10, 10);
        selectedTopicsPane.setPrefWrapLength(600);
        
        // Available topics
        Label availableLabel = new Label("Available:");
        availableLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        
        availableTopicsPane = new FlowPane(10, 10);
        availableTopicsPane.setPrefWrapLength(600);
        
        section.getChildren().addAll(
            label,
            selectedLabel,
            selectedTopicsPane,
            availableLabel,
            availableTopicsPane
        );
        
        return section;
    }
    
    private void loadTopics() {
        TopicService.getAllTopics(token)
            .thenAccept(topics -> {
                javafx.application.Platform.runLater(() -> {
                    availableTopics = topics;
                    displayTopics();
                });
            })
            .exceptionally(error -> {
                System.err.println("Failed to load topics: " + error.getMessage());
                return null;
            });
    }
    
    private void displayTopics() {
        availableTopicsPane.getChildren().clear();
        
        for (Topic topic : availableTopics) {
            if (!selectedTopics.contains(topic)) {
                TopicChip chip = new TopicChip(topic, true, false);
                chip.setOnClick(() -> selectTopic(topic));
                availableTopicsPane.getChildren().add(chip);
            }
        }
    }
    
    private void selectTopic(Topic topic) {
        if (selectedTopics.size() < 5 && !selectedTopics.contains(topic)) {
            selectedTopics.add(topic);
            updateSelectedTopics();
            displayTopics();
        }
    }
    
    private void updateSelectedTopics() {
        selectedTopicsPane.getChildren().clear();
        
        if (selectedTopics.isEmpty()) {
            Label emptyLabel = new Label("No topics selected yet");
            emptyLabel.setFont(Font.font("System", 12));
            emptyLabel.setTextFill(Color.web("#999999"));
            selectedTopicsPane.getChildren().add(emptyLabel);
            return;
        }
        
        for (Topic topic : selectedTopics) {
            TopicChip chip = new TopicChip(topic, false, true);
            chip.setOnRemove(() -> {
                selectedTopics.remove(topic);
                updateSelectedTopics();
                displayTopics();
            });
            selectedTopicsPane.getChildren().add(chip);
        }
    }
    
    private void updateStats() {
        String html = contentEditor.getHtmlText();
        String text = HtmlParser.stripHtml(html);
        int wordCount = ReadingTimeCalculator.getWordCount(text);
        int readingTime = ReadingTimeCalculator.calculateReadingTime(html);
        
        wordCountLabel.setText(wordCount + " words");
        readingTimeLabel.setText(readingTime + " min");
    }
    
    private void saveDraft() {
        String title = titleField.getText().trim();
        String content = contentEditor.getHtmlText();
        
        if (title.isEmpty() && HtmlParser.stripHtml(content).isEmpty()) {
            ErrorHandler.showWarning("Empty Draft", "Please add some content before saving.");
            return;
        }
        
        progressIndicator.setVisible(true);
        
        PostService.CreatePostRequest request = new PostService.CreatePostRequest();
        request.title = title.isEmpty() ? "Untitled" : title;
        request.subtitle = subtitleField.getText().trim();
        request.content = content;
        request.coverImage = coverImageField.getText().trim();
        request.topics = selectedTopics.stream().map(Topic::getTopicId).collect(Collectors.toList());
        request.isDraft = true;
        
        PostService.createPost(request, token)
            .thenAccept(post -> {
                javafx.application.Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    ErrorHandler.showSuccess("Saved", "Draft saved successfully!");
                    goBack();
                });
            })
            .exceptionally(error -> {
                javafx.application.Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    ErrorHandler.handleApiError((Exception) error);
                });
                return null;
            });
    }
    
    private void publish() {
        String title = titleField.getText().trim();
        String content = contentEditor.getHtmlText();
        
        if (title.isEmpty()) {
            ErrorHandler.showWarning("Title Required", "Please add a title to publish.");
            return;
        }
        
        if (HtmlParser.stripHtml(content).isEmpty()) {
            ErrorHandler.showWarning("Content Required", "Please add content to publish.");
            return;
        }
        
        progressIndicator.setVisible(true);
        
        PostService.CreatePostRequest request = new PostService.CreatePostRequest();
        request.title = title;
        request.subtitle = subtitleField.getText().trim();
        request.content = content;
        request.coverImage = coverImageField.getText().trim();
        request.topics = selectedTopics.stream().map(Topic::getTopicId).collect(Collectors.toList());
        request.isDraft = false;
        
        PostService.createPost(request, token)
            .thenAccept(post -> {
                javafx.application.Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    ErrorHandler.showSuccess("Published", "Your story has been published!");
                    goBack();
                });
            })
            .exceptionally(error -> {
                javafx.application.Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    ErrorHandler.handleApiError((Exception) error);
                });
                return null;
            });
    }
    
    private void goBack() {
        DashboardView dashboardView = new DashboardView(stage);
        stage.setScene(dashboardView.createScene());
        stage.setTitle("Dashboard - Blog Platform");
    }
    
    public Scene createScene() {
        return new Scene(this, 1000, 800);
    }
}
