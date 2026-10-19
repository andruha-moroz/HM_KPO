package vyshkat.service;

import vyshkat.domain.RentalVehicle;

/** Decides whether an inspected vehicle can enter the active fleet. */
@FunctionalInterface
public interface ServiceCenter {
    /**
     * Returns whether inspection accepts the given vehicle.
     *
     * @param vehicle vehicle being inspected
     * @return true when the vehicle is accepted
     */
    boolean accepts(RentalVehicle vehicle);
}
