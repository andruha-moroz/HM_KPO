package vyshkat.domain;

/**
 * An electric bicycle in the rental fleet.
 *
 * @param name display name
 * @param inventoryNumber unique fleet number
 * @param beginnerEase beginner suitability score
 * @param dailyEnergyKwh daily energy consumption
 * @param safetyInspectionPassed safety inspection result
 */
public record ElectricBicycle(String name, String inventoryNumber,
                              int beginnerEase, double dailyEnergyKwh,
                              boolean safetyInspectionPassed)
        implements RentalVehicle, EnergyConsumer {
    /** Validates the electric bicycle's identity, rating, and consumption. */
    public ElectricBicycle {
        VehicleRules.validate(name, inventoryNumber, beginnerEase);
        if (!Double.isFinite(dailyEnergyKwh) || dailyEnergyKwh < 0) {
            throw new IllegalArgumentException(
                    "Расход энергии должен быть неотрицательным числом");
        }
    }
}
