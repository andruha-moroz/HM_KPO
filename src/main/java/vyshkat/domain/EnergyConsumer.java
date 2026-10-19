package vyshkat.domain;

/** An object with a measurable daily energy consumption. */
public interface EnergyConsumer {
    /**
     * Returns daily energy consumption in kilowatt-hours.
     *
     * @return daily energy consumption
     */
    double dailyEnergyKwh();
}
