package vyshkat.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record Rental(String number, String customerName,
                     RentalVehicle vehicle, Instant startedAt,
                     Instant returnedAt) {
    /** Validates the rental identity and its time interval. */
    public Rental {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Нужен номер аренды");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Нужно имя клиента");
        }
        Objects.requireNonNull(vehicle);
        Objects.requireNonNull(startedAt);
        if (returnedAt != null && !returnedAt.isAfter(startedAt)) {
            throw new IllegalArgumentException(
                    "Время возврата должно быть позже начала аренды");
        }
    }

    /**
     * Indicates whether the vehicle has not yet been returned.
     *
     * @return true while the rental is active
     */
    public boolean isActive() {
        return returnedAt == null;
    }

    /**
     * Completes the rental at the supplied return time.
     *
     * @param returnTime vehicle return time
     * @return a completed rental
     */
    public Rental complete(final Instant returnTime) {
        if (!isActive()) {
            throw new IllegalStateException("Аренда уже завершена: " + number);
        }
        return new Rental(number, customerName, vehicle, startedAt,
                Objects.requireNonNull(returnTime));
    }

    /**
     * Returns the elapsed duration of a completed rental.
     *
     * @return rental duration
     */
    public Duration duration() {
        if (isActive()) {
            throw new IllegalStateException(
                    "Нельзя узнать длительность незавершённой аренды");
        }
        return Duration.between(startedAt, returnedAt);
    }
}
