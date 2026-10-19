package vyshkat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import vyshkat.console.DemoRunner;
import vyshkat.service.ParkService;
import vyshkat.service.RentalService;
import vyshkat.service.ServiceCenter;

/** Declares application objects and their injected dependencies. */
@Configuration(proxyBeanMethods = false)
@ComponentScan("vyshkat.service")
public final class AppConfig {
    /**
     * Creates the park application service.
     *
     * @param inspectionService inspection decision dependency
     * @return the configured park service
     */
    @Bean
    public ParkService parkService(final ServiceCenter inspectionService) {
        return new ParkService(inspectionService);
    }

    /**
     * Creates the console scenario runner.
     *
     * @param parkService park application service
     * @return the configured scenario runner
     */
    @Bean
    public RentalService rentalService(final ParkService parkService) {
        return new RentalService(parkService);
    }

    /**
     * Creates the console scenario runner.
     *
     * @param parkService park application service
     * @param rentalService rental application service
     * @return the configured scenario runner
     */
    @Bean
    public DemoRunner demoRunner(final ParkService parkService,
                                 final RentalService rentalService) {
        return new DemoRunner(parkService, rentalService);
    }
}
