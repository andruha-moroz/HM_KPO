package vyshkat;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import vyshkat.config.AppConfig;
import vyshkat.console.DemoRunner;

/** Starts the VyshKat console demonstration. */
public final class Main {
    private Main() {
    }

    /**
     * Runs the application using its Spring configuration.
     *
     * @param args unused command-line arguments
     */
    public static void main(final String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            context.getBean(DemoRunner.class).run();
        }
    }
}
