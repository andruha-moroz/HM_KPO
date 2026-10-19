package vyshkat.service;

import org.junit.jupiter.api.Test;
import vyshkat.domain.Scooter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests the default inspection decision. */
class DefaultServiceCenterTest {
    private static final int EASE_SCORE = 7;
    private static final double DAILY_ENERGY = 1.0;

    @Test
    void acceptsVehicleWhenSafetyCheckPassed() {
        DefaultServiceCenter serviceCenter = new DefaultServiceCenter();
        Scooter scooter = new Scooter("Самокат", "A-1", EASE_SCORE,
                DAILY_ENERGY, true);

        assertTrue(serviceCenter.accepts(scooter));
    }

    @Test
    void rejectsVehicleWhenSafetyCheckFailed() {
        DefaultServiceCenter serviceCenter = new DefaultServiceCenter();
        Scooter scooter = new Scooter("Самокат", "A-1", EASE_SCORE,
                DAILY_ENERGY, false);

        assertFalse(serviceCenter.accepts(scooter));
    }
}
