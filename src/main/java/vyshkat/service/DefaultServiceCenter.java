package vyshkat.service;

import org.springframework.stereotype.Component;
import vyshkat.domain.RentalVehicle;

/** Accepts vehicles whose safety check has passed. */
@Component
public final class DefaultServiceCenter implements ServiceCenter {
    /** Creates the default inspection decision service. */
    public DefaultServiceCenter() {
    }

    /** {@inheritDoc} */
    @Override
    public boolean accepts(final RentalVehicle vehicle) {
        return vehicle.safetyInspectionPassed();
    }
}
