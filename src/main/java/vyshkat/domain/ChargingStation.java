package vyshkat.domain;

/**
 * An inventory-tracked charging station with daily energy use.
 *
 * @param name display name
 * @param inventoryNumber unique inventory number
 * @param dailyEnergyKwh daily energy consumption
 */
public record ChargingStation(String name, String inventoryNumber,
                              double dailyEnergyKwh)
        implements InventoryItem, EnergyConsumer {
    /** Validates the station identity and energy consumption. */
    public ChargingStation {
        if (name == null || name.isBlank()
                || inventoryNumber == null || inventoryNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Для вещи нужны название и инвентарный номер");
        }
        if (!Double.isFinite(dailyEnergyKwh) || dailyEnergyKwh < 0) {
            throw new IllegalArgumentException(
                    "Расход энергии должен быть неотрицательным числом");
        }
    }
}
