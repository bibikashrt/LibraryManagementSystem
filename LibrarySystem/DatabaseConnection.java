
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // private static final String URL
    //         = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/library_db");
    // private static final String USER
    //         = System.getenv().getOrDefault("DB_USER", "postgres");
    // private static final String PASSWORD
    //         = System.getenv().getOrDefault("DB_PASSWORD", "");
    // private DatabaseConnection() {
    // }
    private static final String URL
            = "jdbc:postgresql://localhost:5432/library_db";

    private static final String USER = "postgres";

    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
