package vyshkat.domain;

/** A rental vehicle rated for suitability for beginners. */
public interface RentalVehicle extends InventoryItem {
    int BEGINNER_EASE_THRESHOLD = 6;

    /**
     * Returns the ease score from 1 to 10.
     *
     * @return the ease score
     */
    int beginnerEase();

    /**
     * Returns whether the safety inspection passed.
     *
     * @return true when the safety inspection passed
     */
    boolean safetyInspectionPassed();

    /**
     * Indicates whether the vehicle meets the beginner score threshold.
     *
     * @return true when the score meets the threshold
     */
    default boolean isSuitableForBeginners() {
        return beginnerEase() >= BEGINNER_EASE_THRESHOLD;
    }
}
