package dao;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstadoCajeroDAO {

    public double obtenerCantidadDinero() throws SQLException {
        String sql = "SELECT cantidad_dinero FROM EstadoCajero WHERE id = 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getDouble("cantidad_dinero");
            }
        }
        return 0.0;
    }

    public void actualizarCantidadDinero(double cantidad) throws SQLException {
        String sql = "UPDATE EstadoCajero SET cantidad_dinero = ? WHERE id = 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, cantidad);
            statement.executeUpdate();
        }
    }
}
