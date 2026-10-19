package vyshkat.service;

import org.junit.jupiter.api.Test;
import vyshkat.domain.Scooter;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RentalServiceTest {
    private static final int RENTAL_MINUTES = 45;
    private static final int VEHICLE_EASE = 8;
    private static final double VEHICLE_ENERGY = 1.2;
    private static final Instant START = Instant.parse("2026-10-19T09:00:00Z");
    private static final Instant RETURN = Instant.parse("2026-10-19T09:45:00Z");

    @Test
    void startsRentalAndRecordsActiveRental() {
        RentalService service = serviceWithVehicle();

        var rental = service.startRental("Анна Петрова", "ВК-001", START);

        assertEquals("АР-001", rental.number());
        assertEquals("Анна Петрова", rental.customerName());
        assertEquals("ВК-001", rental.vehicle().inventoryNumber());
        assertTrue(rental.isActive());
        assertEquals(1, service.activeRentals().size());
    }

    @Test
    void refusesVehicleNotAdmittedToPark() {
        RentalService service = new RentalService(new ParkService(
                vehicle -> true));

        assertThrows(IllegalArgumentException.class,
                () -> service.startRental("Анна", "ВК-404", START));
    }

    @Test
    void refusesToRentSameVehicleTwiceAtOnce() {
        RentalService service = serviceWithVehicle();
        service.startRental("Анна", "ВК-001", START);

        assertThrows(IllegalStateException.class,
                () -> service.startRental("Борис", "ВК-001", START));
    }

    @Test
    void completesRentalAndMakesVehicleAvailableAgain() {
        RentalService service = serviceWithVehicle();
        var first = service.startRental("Анна", "ВК-001", START);

        var completed = service.finishRental(first.number(), RETURN);

        assertFalse(completed.isActive());
        assertEquals(Duration.ofMinutes(RENTAL_MINUTES), completed.duration());
        assertTrue(service.activeRentals().isEmpty());
        var second = service.startRental("Борис", "ВК-001",
                RETURN.plusSeconds(1));
        assertEquals("АР-002", second.number());
        assertEquals(2, service.allRentals().size());
    }

    @Test
    void refusesUnknownRental() {
        RentalService service = serviceWithVehicle();

        assertThrows(IllegalArgumentException.class,
                () -> service.finishRental("АР-404", RETURN));
    }

    @Test
    void refusesFinishingRentalTwice() {
        RentalService service = serviceWithVehicle();
        var rental = service.startRental("Анна", "ВК-001", START);
        service.finishRental(rental.number(), RETURN);

        assertThrows(IllegalStateException.class,
                () -> service.finishRental(rental.number(),
                        RETURN.plusSeconds(1)));
    }

    @Test
    void refusesReturnBeforeRentalStart() {
        RentalService service = serviceWithVehicle();
        var rental = service.startRental("Анна", "ВК-001", START);

        assertThrows(IllegalArgumentException.class,
                () -> service.finishRental(rental.number(), START));
    }

    @Test
    void refusesBlankCustomerName() {
        RentalService service = serviceWithVehicle();

        assertThrows(IllegalArgumentException.class,
                () -> service.startRental(" ", "ВК-001", START));
    }

    private RentalService serviceWithVehicle() {
        ParkService park = new ParkService(vehicle -> true);
        park.acceptVehicle(new Scooter("Электросамокат", "ВК-001",
                VEHICLE_EASE, VEHICLE_ENERGY, true));
        return new RentalService(park);
    }
}
