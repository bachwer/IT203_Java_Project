package util;

import java.math.BigDecimal;

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


}

