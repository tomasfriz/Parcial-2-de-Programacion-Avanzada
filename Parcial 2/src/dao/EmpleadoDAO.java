package dao;

import model.Empleado;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpleadoDAO {

    public Empleado obtenerEmpleadoPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Empleados WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Empleado empleado = new Empleado();
                    empleado.setId(resultSet.getInt("id"));
                    empleado.setNombre(resultSet.getString("nombre"));
                    empleado.setApellido(resultSet.getString("apellido"));
                    empleado.setEmail(resultSet.getString("email"));
                    empleado.setPassword(resultSet.getString("password"));
                    return empleado;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener el empleado por email", e);
        }
        return null;
    }
}
