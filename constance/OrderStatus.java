package constance;

public enum OrderStatus {
    CheckIn,
    CheckOuted;

    public static OrderStatus fromDb(String value) {
        if (value == null) {
            return CheckIn;
        }

        String normalized = value.trim();
        if (normalized.equalsIgnoreCase("CheckOuted")) {
            return CheckOuted;
        }
        return CheckIn;
    }
}
