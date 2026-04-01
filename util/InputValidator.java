package util;

import java.math.BigDecimal;
import java.util.Scanner;

public final class InputValidator {
    private InputValidator() {
    }

    public static boolean isNotBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isPositiveInt(int value) {
        return value <= 0;
    }

    public static boolean isPositivePrice(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isRatingValid(int rating) {
        return rating >= 1 && rating <= 5;
    }

    public static Integer readInt(Scanner scanner, String prompt) {
        if (scanner == null) {
            return null;
        }
        System.out.print(prompt);
        String raw = scanner.nextLine().trim();
        if (raw.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal readBigDecimal(Scanner scanner, String prompt) {
        if (scanner == null) {
            return null;
        }
        System.out.print(prompt);
        String raw = scanner.nextLine().trim();
        if (raw.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String readNonBlank(Scanner scanner, String prompt) {
        if (scanner == null) {
            return null;
        }
        System.out.print(prompt);
        String raw = scanner.nextLine();
        if (raw == null) {
            return null;
        }
        String value = raw.trim();
        return value.isEmpty() ? null : value;
    }

    public static boolean isYes(String value) {
        return value != null && value.trim().equalsIgnoreCase("Y");
    }


}

