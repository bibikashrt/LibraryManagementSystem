package config;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class LoggerConfig {

    public static final Logger LOGGER
            = Logger.getLogger("LibraryManagementSystem");

    static {

        try {

            FileHandler fileHandler = new FileHandler("library.log", true);

            fileHandler.setFormatter(
                    new SimpleFormatter());

            LOGGER.addHandler(fileHandler);

        } catch (IOException e) {

            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    private LoggerConfig() {
    }
}
