package org.example.projet4dx.util;

public class StringEscapeUtils {

    /**
     * Sanitizes a given input string by replacing special characters with their HTML entity equivalents.
     *
     * @param input the input string to be sanitized
     * @return the sanitized string with special characters replaced by HTML entities
     */
    public static String sanitizeString(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder sanitizedString = new StringBuilder();
        for (char c : input.toCharArray()) {
            switch (c) {
                case '<':
                    sanitizedString.append("&lt;");
                    break;
                case '>':
                    sanitizedString.append("&gt;");
                    break;
                case '\"':
                    sanitizedString.append("&quot;");
                    break;
                case '\'':
                    sanitizedString.append("&#x27;");
                    break;
                case '&':
                    sanitizedString.append("&amp;");
                    break;
                default:
                    sanitizedString.append(c);
                    break;
            }
        }
        return sanitizedString.toString();
    }
}
