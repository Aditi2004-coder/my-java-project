import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() {
        Connection conn = null;

        try {
            // MySQL details
            String url = "jdbc:mysql://localhost:3306/userformdb"; // your DB name
            String username = "root";  // your MySQL username
            String password = "root";      // your MySQL password (keep empty if none)

            // Load driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create connection
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database Connected Successfully!");

        } catch (Exception e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }

        return conn;
    }
}