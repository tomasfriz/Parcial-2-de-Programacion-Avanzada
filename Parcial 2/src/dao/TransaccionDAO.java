package dao;

import model.Transaccion;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransaccionDAO {

    public void registrarTransaccion(Transaccion transaccion) throws SQLException {
        String sql = "INSERT INTO Transacciones (cliente_id, tipo, monto) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transaccion.getClienteId());
            statement.setString(2, transaccion.getTipo());
            statement.setDouble(3, transaccion.getMonto());
            statement.executeUpdate();
        }
    }
}
