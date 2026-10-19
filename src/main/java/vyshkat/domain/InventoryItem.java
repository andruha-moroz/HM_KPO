package vyshkat.domain;

/** An object recorded on the organization's balance. */
public interface InventoryItem {
    /**
     * Returns the display name of this object.
     *
     * @return the object name
     */
    String name();

    /**
     * Returns the inventory number of this object.
     *
     * @return the inventory number
     */
    String inventoryNumber();
}
