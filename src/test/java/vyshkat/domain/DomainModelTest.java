package vyshkat.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests domain validation and behavior. */
class DomainModelTest {
    private static final int MINIMUM_SCORE = 1;
    private static final int THRESHOLD_SCORE = 6;
    private static final int MAXIMUM_SCORE = 10;
    private static final int INVALID_SCORE = 11;
    private static final double DAILY_ENERGY = 1.5;

    @Test
    void beginnerThresholdIncludesSix() {
        Bicycle bicycle = new Bicycle(
                "Городской", "A-1", THRESHOLD_SCORE, true);

        assertTrue(bicycle.isSuitableForBeginners());
    }

    @Test
    void beginnerThresholdExcludesLowerScore() {
        Bicycle bicycle = new Bicycle(
                "Городской", "A-1", MINIMUM_SCORE, true);

        assertFalse(bicycle.isSuitableForBeginners());
    }

    @Test
    void acceptsMaximumEaseScoreAndStoresEnergy() {
        Scooter scooter = new Scooter("Самокат", "A-1", MAXIMUM_SCORE,
                DAILY_ENERGY, true);

        assertEquals(DAILY_ENERGY, scooter.dailyEnergyKwh());
    }

    @Test
    void rejectsMissingVehicleName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Bicycle(" ", "A-1", MINIMUM_SCORE, true));
    }

    @Test
    void rejectsMissingVehicleInventoryNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> new Bicycle("Велосипед", " ", MINIMUM_SCORE, true));
    }

    @Test
    void rejectsEaseScoreAboveTen() {
        assertThrows(IllegalArgumentException.class,
                () -> new Bicycle("Велосипед", "A-1", INVALID_SCORE, true));
    }

    @Test
    void rejectsNegativeEnergyConsumption() {
        assertThrows(IllegalArgumentException.class,
                () -> new ChargingStation(
                        "Станция", "I-1", -DAILY_ENERGY));
    }

    @Test
    void rejectsMissingItemNameOrInventoryNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> new Helmet("Шлем", " "));
        assertThrows(IllegalArgumentException.class,
                () -> new Helmet(" ", "I-1"));
    }
}
