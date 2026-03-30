package util;

import java.util.Locale;

public final class CliColor {
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String RED = "\u001B[31m";
    private static final String MAGENTA = "\u001B[35m";

    private CliColor() {
    }

    public static String title(String text) {
        return wrap(text, BOLD + CYAN);
    }

    public static String header(String text) {
        return wrap(text, BOLD + YELLOW);
    }

    public static String byStatus(String text) {
        if (text == null) {
            return "";
        }

        String status = text.trim().toUpperCase(Locale.ROOT);
        return switch (status) {
            case "AVAILABLE", "DONE", "COMPLETED", "APPROVED", "CHECKOUTED" -> wrap(text, GREEN);
            case "PENDING", "COOKING", "PROCESSING", "OCCUPIED", "CHECKIN" -> wrap(text, BLUE);
            case "OUT_OF_STOCK", "CANCELLED", "DISABLED", "BANNED" -> wrap(text, RED);
            default -> wrap(text, MAGENTA);
        };
    }

    public static String stripAnsi(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("\\u001B\\[[;\\d]*m", "");
    }

    private static String wrap(String text, String colorCode) {
        if (!isEnabled() || text == null || text.isEmpty()) {
            return text;
        }
        return colorCode + text + RESET;
    }

    private static boolean isEnabled() {
        return System.console() != null && System.getenv("NO_COLOR") == null;
    }
}

