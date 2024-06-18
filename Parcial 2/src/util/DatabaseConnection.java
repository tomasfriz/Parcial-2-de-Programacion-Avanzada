package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/banco";
    private static final String USER = "root";
    // Cambiar la contraseña con la contraseña de su base de datos
    private static final String PASSWORD = "programador821";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
