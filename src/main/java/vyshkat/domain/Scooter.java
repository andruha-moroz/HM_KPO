package vyshkat.domain;

/**
 * An electric scooter in the rental fleet.
 *
 * @param name display name
 * @param inventoryNumber unique fleet number
 * @param beginnerEase beginner suitability score
 * @param dailyEnergyKwh daily energy consumption
 * @param safetyInspectionPassed safety inspection result
 */
public record Scooter(String name, String inventoryNumber, int beginnerEase,
                      double dailyEnergyKwh, boolean safetyInspectionPassed)
        implements RentalVehicle, EnergyConsumer {
    /** Validates the scooter's identity, rating, and energy consumption. */
    public Scooter {
        VehicleRules.validate(name, inventoryNumber, beginnerEase);
        if (!Double.isFinite(dailyEnergyKwh) || dailyEnergyKwh < 0) {
            throw new IllegalArgumentException(
                    "Расход энергии должен быть неотрицательным числом");
        }
    }
}
