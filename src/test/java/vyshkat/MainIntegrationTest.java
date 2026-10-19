package vyshkat;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests the complete console demonstration and Spring wiring. */
class MainIntegrationTest {
    @Test
    void runsAllScenariosThroughTheSpringContainer() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream capturedOut = new PrintStream(
                output, true, StandardCharsets.UTF_8)) {
            System.setOut(capturedOut);
            Main.main(new String[0]);
        } finally {
            System.setOut(originalOut);
        }
        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("Самокат ВК-001 — принято техосмотром"));
        assertTrue(result.contains(
                "Электровелосипед ВК-002 — отказ техосмотра"));
        assertTrue(result.contains("Всего единиц на балансе: 4"));
        assertTrue(result.contains("Суточное энергопотребление: 2.0 кВт·ч"));
        assertTrue(result.contains("Шлем [ВЕЩЬ-001]"));
        assertTrue(result.contains("Анна Петрова взял транспорт ВК-001"));
        assertTrue(result.contains("аренда длилась 45 мин."));
    }
}
