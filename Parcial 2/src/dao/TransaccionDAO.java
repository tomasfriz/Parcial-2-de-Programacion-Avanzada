package dao;

import model.Transaccion;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Transaccion> obtenerMovimientosPorCliente(int clienteId) throws SQLException {
        String sql = "SELECT * FROM Transacciones WHERE cliente_id = ?";
        List<Transaccion> transacciones = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clienteId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Transaccion transaccion = new Transaccion();
                transaccion.setId(resultSet.getInt("id"));
                transaccion.setClienteId(resultSet.getInt("cliente_id"));
                transaccion.setTipo(resultSet.getString("tipo"));
                transaccion.setMonto(resultSet.getDouble("monto"));
                transaccion.setFecha(resultSet.getTimestamp("fecha"));
                transacciones.add(transaccion);
            }
        }
        return transacciones;
    }

    public List<Transaccion> obtenerTodasLasTransacciones() throws SQLException {
        String sql = "SELECT * FROM Transacciones";
        List<Transaccion> transacciones = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Transaccion transaccion = new Transaccion();
                transaccion.setId(resultSet.getInt("id"));
                transaccion.setClienteId(resultSet.getInt("cliente_id"));
                transaccion.setTipo(resultSet.getString("tipo"));
                transaccion.setMonto(resultSet.getDouble("monto"));
                transaccion.setFecha(resultSet.getTimestamp("fecha"));
                transacciones.add(transaccion);
            }
        }
        return transacciones;
    }
}
