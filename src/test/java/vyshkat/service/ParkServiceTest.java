package vyshkat.service;

import org.junit.jupiter.api.Test;
import vyshkat.domain.Bicycle;
import vyshkat.domain.ChargingStation;
import vyshkat.domain.Helmet;
import vyshkat.domain.Scooter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests park registration and reporting behavior. */
class ParkServiceTest {
    private static final int BEGINNER_SCORE = 6;
    private static final int NON_BEGINNER_SCORE = 5;
    private static final int HIGH_SCORE = 8;
    private static final double VEHICLE_ENERGY = 1.2;
    private static final double STATION_ENERGY = 0.8;
    private static final double EXPECTED_ENERGY = 2.0;
    private static final double ENERGY_DELTA = 0.0001;

    @Test
    void acceptsVehicleWhenInjectedInspectionCenterApprovesIt() {
        ParkService park = new ParkService(vehicle -> true);

        boolean accepted = park.acceptVehicle(new Scooter(
                "Самокат", "A-1", HIGH_SCORE, VEHICLE_ENERGY, false));

        assertTrue(accepted);
        assertEquals(1, park.itemCount());
    }

    @Test
    void doesNotAddVehicleWhenInjectedInspectionCenterRejectsIt() {
        ParkService park = new ParkService(vehicle -> false);

        boolean accepted = park.acceptVehicle(new Scooter(
                "Самокат", "A-1", HIGH_SCORE, VEHICLE_ENERGY, true));

        assertFalse(accepted);
        assertEquals(0, park.itemCount());
    }

    @Test
    void listsVehiclesAtThresholdAndSumsEnergyConsumers() {
        ParkService park = new ParkService(vehicle -> true);
        park.acceptVehicle(new Scooter(
                "Удобный", "A-1", BEGINNER_SCORE, VEHICLE_ENERGY, true));
        park.acceptVehicle(new Bicycle(
                "Сложный", "A-2", NON_BEGINNER_SCORE, true));
        park.addItem(new ChargingStation(
                "Зарядная станция", "I-1", STATION_ENERGY));
        park.addItem(new Helmet("Шлем", "I-2"));

        assertEquals(1, park.beginnerFriendlyVehicles().size());
        assertEquals("A-1", park.beginnerFriendlyVehicles().getFirst()
                .inventoryNumber());
        assertEquals(EXPECTED_ENERGY, park.totalDailyEnergyKwh(), ENERGY_DELTA);
    }

    @Test
    void rejectsDuplicateInventoryNumbersAcrossItemTypes() {
        ParkService park = new ParkService(vehicle -> true);
        park.addItem(new Helmet("Шлем", "I-1"));

        assertThrows(IllegalArgumentException.class,
                () -> park.addItem(new ChargingStation(
                        "Станция", "I-1", STATION_ENERGY)));
    }

    @Test
    void requiresVehiclesToPassThroughInspection() {
        ParkService park = new ParkService(vehicle -> true);
        Bicycle bicycle = new Bicycle("Велосипед", "A-2", BEGINNER_SCORE, true);

        assertThrows(IllegalArgumentException.class,
                () -> park.addItem(bicycle));
    }
}
