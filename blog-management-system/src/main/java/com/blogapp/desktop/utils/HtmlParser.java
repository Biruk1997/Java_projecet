package com.blogapp.desktop.utils;

/**
 * HtmlParser - HTML parsing and sanitization utilities
 */
public class HtmlParser {
    
    /**
     * Strip all HTML tags from text
     */
    public static String stripHtml(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }
        return html.replaceAll("<[^>]*>", "").trim();
    }
    
    /**
     * Get excerpt from HTML content
     */
    public static String getExcerpt(String html, int maxLength) {
        String text = stripHtml(html);
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
    
    /**
     * Sanitize HTML (basic implementation)
     */
    public static String sanitizeHtml(String html) {
        if (html == null) {
            return "";
        }
        // Remove script tags
        html = html.replaceAll("<script[^>]*>.*?</script>", "");
        // Remove style tags
        html = html.replaceAll("<style[^>]*>.*?</style>", "");
        // Remove event handlers
        html = html.replaceAll("on\\w+=\"[^\"]*\"", "");
        return html;
    }
}
