package vyshkat.service;

import vyshkat.domain.EnergyConsumer;
import vyshkat.domain.InventoryItem;
import vyshkat.domain.RentalVehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Manages park assets, inspection, and fleet reports. */
public final class ParkService {
    private final ServiceCenter serviceCenter;

    private final List<InventoryItem> items = new ArrayList<>();

    /**
     * Creates the service with an inspection decision dependency.
     *
     * @param inspectionService inspection dependency
     */
    public ParkService(final ServiceCenter inspectionService) {
        serviceCenter = Objects.requireNonNull(inspectionService);
    }

    /**
     * Inspects and, if approved, adds a vehicle to the park.
     *
     * @param vehicle vehicle to inspect
     * @return true if the vehicle was accepted
     */
    public boolean acceptVehicle(final RentalVehicle vehicle) {
        ensureInventoryNumberIsAvailable(vehicle);
        if (!serviceCenter.accepts(vehicle)) {
            return false;
        }
        items.add(vehicle);
        return true;
    }

    /**
     * Adds a non-vehicle inventory item to the park.
     *
     * @param item item to register
     */
    public void addItem(final InventoryItem item) {
        if (item instanceof RentalVehicle) {
            throw new IllegalArgumentException(
                    "Транспорт принимается только через техосмотр");
        }
        ensureInventoryNumberIsAvailable(item);
        items.add(item);
    }


    /**
     * Finds an admitted rental vehicle by its inventory number.
     *
     * @param inventoryNumber vehicle inventory number
     * @return the admitted vehicle
     */
    public RentalVehicle rentalVehicle(final String inventoryNumber) {
        if (inventoryNumber == null || inventoryNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Нужен инвентарный номер транспорта");
        }
        return items.stream()
                .filter(RentalVehicle.class::isInstance)
                .map(RentalVehicle.class::cast)
                .filter(vehicle -> vehicle.inventoryNumber()
                        .equals(inventoryNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Транспорт не принят в парк: " + inventoryNumber));
    }

    /**
     * Returns the number of registered park assets.
     *
     * @return registered item count
     */
    public int itemCount() {
        return items.size();
    }

    /**
     * Returns total daily energy use across all park consumers.
     *
     * @return daily energy consumption in kilowatt-hours
     */
    public double totalDailyEnergyKwh() {
        return items.stream()
                .filter(EnergyConsumer.class::isInstance)
                .map(EnergyConsumer.class::cast)
                .mapToDouble(EnergyConsumer::dailyEnergyKwh)
                .sum();
    }

    /**
     * Returns accepted rental vehicles suitable for beginners.
     *
     * @return beginner-friendly vehicles
     */
    public List<RentalVehicle> beginnerFriendlyVehicles() {
        return items.stream()
                .filter(RentalVehicle.class::isInstance)
                .map(RentalVehicle.class::cast)
                .filter(RentalVehicle::isSuitableForBeginners)
                .toList();
    }

    /**
     * Returns an immutable snapshot of all registered items.
     *
     * @return registered items
     */
    public List<InventoryItem> allItems() {
        return List.copyOf(items);
    }

    private void ensureInventoryNumberIsAvailable(
            final InventoryItem candidate) {
        Objects.requireNonNull(candidate);
        boolean alreadyUsed = items.stream()
                .anyMatch(item -> item.inventoryNumber()
                        .equals(candidate.inventoryNumber()));
        if (alreadyUsed) {
            throw new IllegalArgumentException(
                    "Инвентарный номер уже используется: "
                            + candidate.inventoryNumber());
        }
    }
}
