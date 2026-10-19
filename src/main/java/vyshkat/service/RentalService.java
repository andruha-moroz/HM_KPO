package vyshkat.service;

import vyshkat.domain.Rental;
import vyshkat.domain.RentalVehicle;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Starts, completes, and lists vehicle rentals. */
public final class RentalService {
    private final ParkService parkService;
    private final List<Rental> rentals = new ArrayList<>();
    private int nextNumber = 1;

    /**
     * Creates the rental service for one park.
     *
     * @param fleetService service that provides admitted vehicles
     */
    public RentalService(final ParkService fleetService) {
        parkService = Objects.requireNonNull(fleetService);
    }

    /**
     * Starts renting an admitted vehicle that is not currently rented.
     *
     * @param customerName customer name
     * @param inventoryNumber vehicle inventory number
     * @param startedAt explicit rental start time
     * @return the new active rental
     */
    public Rental startRental(final String customerName,
                              final String inventoryNumber,
                              final Instant startedAt) {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Нужно имя клиента");
        }
        Objects.requireNonNull(startedAt);
        RentalVehicle vehicle = parkService.rentalVehicle(inventoryNumber);
        boolean alreadyRented = rentals.stream()
                .anyMatch(rental -> rental.isActive()
                        && rental.vehicle().inventoryNumber()
                        .equals(inventoryNumber));
        if (alreadyRented) {
            throw new IllegalStateException(
                    "Транспорт уже находится в аренде: " + inventoryNumber);
        }
        String number = String.format(Locale.ROOT, "АР-%03d", nextNumber++);
        Rental rental = new Rental(number, customerName, vehicle, startedAt,
                null);
        rentals.add(rental);
        return rental;
    }

    /**
     * Returns the vehicle and completes its active rental.
     *
     * @param rentalNumber rental identifier
     * @param returnedAt explicit return time
     * @return the completed rental
     */
    public Rental finishRental(final String rentalNumber,
                               final Instant returnedAt) {
        if (rentalNumber == null || rentalNumber.isBlank()) {
            throw new IllegalArgumentException("Нужен номер аренды");
        }
        Objects.requireNonNull(returnedAt);
        for (int index = 0; index < rentals.size(); index++) {
            Rental rental = rentals.get(index);
            if (rental.number().equals(rentalNumber)) {
                Rental completed = rental.complete(returnedAt);
                rentals.set(index, completed);
                return completed;
            }
        }
        throw new IllegalArgumentException(
                "Аренда не найдена: " + rentalNumber);
    }

    /**
     * Returns an immutable snapshot of all rentals, including completed ones.
     *
     * @return rental history
     */
    public List<Rental> allRentals() {
        return List.copyOf(rentals);
    }

    /**
     * Returns all rentals that have not yet been completed.
     *
     * @return active rentals
     */
    public List<Rental> activeRentals() {
        return rentals.stream().filter(Rental::isActive).toList();
    }
}
