package vyshkat.console;

import vyshkat.domain.Bicycle;
import vyshkat.domain.ChargingStation;
import vyshkat.domain.ElectricBicycle;
import vyshkat.domain.Helmet;
import vyshkat.domain.InventoryItem;
import vyshkat.domain.RentalVehicle;
import vyshkat.domain.Scooter;
import vyshkat.service.ParkService;
import vyshkat.service.RentalService;

import java.time.Instant;
import java.util.Locale;

/** Runs the console demonstration scenarios for the park. */
public final class DemoRunner {
    private static final int SCOOTER_EASE = 8;

    private static final double SCOOTER_ENERGY = 1.2;

    private static final int E_BIKE_EASE = 7;

    private static final double E_BIKE_ENERGY = 2.4;

    private static final int BICYCLE_EASE = 6;

    private static final double STATION_ENERGY = 0.8;

    private static final String ENERGY_FORMAT =
            "Суточное энергопотребление: %.1f кВт·ч%n";

    private final ParkService park;

    private final RentalService rentals;

    /**
     * Creates the runner with park and rental services.
     *
     * @param parkService park application service
     * @param rentalService rental application service
     */
    public DemoRunner(final ParkService parkService,
                      final RentalService rentalService) {
        park = parkService;
        rentals = rentalService;
    }

    /** Executes all scenarios and prints their results. */
    public void run() {
        System.out.println("=== ВышКат: демонстрация учёта парка ===");
        report("Самокат ВК-001", park.acceptVehicle(
                new Scooter("Электросамокат", "ВК-001", SCOOTER_EASE,
                        SCOOTER_ENERGY, true)));
        report("Электровелосипед ВК-002", park.acceptVehicle(
                new ElectricBicycle("Электровелосипед", "ВК-002",
                        E_BIKE_EASE, E_BIKE_ENERGY, false)));
        report("Велосипед ВК-003", park.acceptVehicle(
                new Bicycle("Городской велосипед", "ВК-003",
                        BICYCLE_EASE, true)));
        park.addItem(new Helmet("Шлем", "ВЕЩЬ-001"));
        park.addItem(new ChargingStation("Зарядная станция", "ВЕЩЬ-002",
                STATION_ENERGY));

        demonstrateRental();
        System.out.println();
        System.out.println("Всего единиц на балансе: " + park.itemCount());
        System.out.printf(Locale.ROOT, ENERGY_FORMAT,
                park.totalDailyEnergyKwh());
        System.out.println();
        System.out.println("Техника для новичков:");
        park.beginnerFriendlyVehicles().forEach(DemoRunner::printVehicle);
        System.out.println();
        System.out.println("Весь транспорт и вещи:");
        park.allItems().forEach(DemoRunner::printItem);
    }

    private void demonstrateRental() {
        Instant start = Instant.parse("2026-10-19T09:00:00Z");
        Instant returned = Instant.parse("2026-10-19T09:45:00Z");
        var rental = rentals.startRental("Анна Петрова", "ВК-001", start);
        System.out.printf("Начало аренды %s: %s взял транспорт %s%n",
                rental.number(), rental.customerName(),
                rental.vehicle().inventoryNumber());
        var completed = rentals.finishRental(rental.number(), returned);
        System.out.printf("Возврат %s; аренда длилась %d мин.%n",
                completed.vehicle().inventoryNumber(),
                completed.duration().toMinutes());
    }

    private static void report(final String description,
                               final boolean accepted) {
        System.out.println(description + (accepted
                ? " — принято техосмотром" : " — отказ техосмотра"));
    }

    private static void printVehicle(final RentalVehicle vehicle) {
        System.out.printf("- %s [%s], простота: %d/10%n",
                vehicle.name(), vehicle.inventoryNumber(),
                vehicle.beginnerEase());
    }

    private static void printItem(final InventoryItem item) {
        System.out.printf("- %s [%s]%n", item.name(), item.inventoryNumber());
    }
}
