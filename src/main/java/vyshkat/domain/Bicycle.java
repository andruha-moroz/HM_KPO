package vyshkat.domain;

/**
 * A non-electric bicycle in the rental fleet.
 *
 * @param name display name
 * @param inventoryNumber unique fleet number
 * @param beginnerEase beginner suitability score
 * @param safetyInspectionPassed safety inspection result
 */
public record Bicycle(String name, String inventoryNumber, int beginnerEase,
                      boolean safetyInspectionPassed) implements RentalVehicle {
    /** Validates the bicycle's identity and beginner rating. */
    public Bicycle {
        VehicleRules.validate(name, inventoryNumber, beginnerEase);
    }
}
