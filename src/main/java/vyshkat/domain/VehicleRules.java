package vyshkat.domain;

/** Shared validation for rental vehicle values. */
final class VehicleRules {
    private static final int MIN_EASE_SCORE = 1;

    private static final int MAX_EASE_SCORE = 10;

    private VehicleRules() {
    }

    /**
     * Validates the common identity and beginner score fields.
     *
     * @param name vehicle name
     * @param inventoryNumber vehicle inventory number
     * @param beginnerEase beginner ease score
     */
    static void validate(final String name, final String inventoryNumber,
                         final int beginnerEase) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "У транспорта должно быть название");
        }
        if (inventoryNumber == null || inventoryNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "У транспорта должен быть инвентарный номер");
        }
        if (beginnerEase < MIN_EASE_SCORE || beginnerEase > MAX_EASE_SCORE) {
            throw new IllegalArgumentException(
                    "Простота для новичка должна быть от 1 до 10");
        }
    }
}
