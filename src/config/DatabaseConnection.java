package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final Properties properties = new Properties();

    static {

        try (InputStream input
                = DatabaseConnection.class
                        .getClassLoader()
                        .getResourceAsStream("config.properties")) {

                    if (input == null) {
                        throw new RuntimeException(
                                "config.properties not found");
                    }

                    properties.load(input);

                } catch (IOException e) {

                    throw new RuntimeException(
                            "Failed to load config.properties",
                            e);
                }
    }

    private DatabaseConnection() {
    }

    public static Connection getConnection()
            throws SQLException {

        String url
                = properties.getProperty("db.url");

        String user
                = properties.getProperty("db.user");

        String password
                = properties.getProperty("db.password");

        return DriverManager.getConnection(
                url,
                user,
                password);
    }
}
