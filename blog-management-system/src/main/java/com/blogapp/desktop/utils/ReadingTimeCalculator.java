package com.blogapp.desktop.utils;

/**
 * ReadingTimeCalculator - Calculate reading time for content
 */
public class ReadingTimeCalculator {
    
    private static final int WORDS_PER_MINUTE = 200;
    
    /**
     * Calculate reading time in minutes
     */
    public static int calculateReadingTime(String content) {
        if (content == null || content.isEmpty()) {
            return 1;
        }
        
        String text = HtmlParser.stripHtml(content);
        int wordCount = getWordCount(text);
        int minutes = Math.max(1, wordCount / WORDS_PER_MINUTE);
        
        return minutes;
    }
    
    /**
     * Get word count
     */
    public static int getWordCount(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        
        String[] words = text.trim().split("\\s+");
        return words.length;
    }
    
    /**
     * Get character count (excluding HTML)
     */
    public static int getCharacterCount(String content) {
        String text = HtmlParser.stripHtml(content);
        return text.length();
    }
    
    /**
     * Format reading time as string
     */
    public static String formatReadingTime(int minutes) {
        if (minutes < 1) {
            return "< 1 min read";
        } else if (minutes == 1) {
            return "1 min read";
        } else {
            return minutes + " min read";
        }
    }
}
