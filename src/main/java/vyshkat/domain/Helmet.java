package vyshkat.domain;

/**
 * An inventory-tracked helmet.
 *
 * @param name display name
 * @param inventoryNumber unique inventory number
 */
public record Helmet(String name, String inventoryNumber)
        implements InventoryItem {
    /** Validates the helmet's name and inventory number. */
    public Helmet {
        if (name == null || name.isBlank()
                || inventoryNumber == null || inventoryNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Для вещи нужны название и инвентарный номер");
        }
    }
}
