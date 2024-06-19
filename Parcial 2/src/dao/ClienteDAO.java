package dao;

import model.Cliente;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {

    public Cliente obtenerClientePorEmail(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Clientes WHERE email = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getInt("id"));
                cliente.setNombre(resultSet.getString("nombre"));
                cliente.setApellido(resultSet.getString("apellido"));
                cliente.setEmail(resultSet.getString("email"));
                cliente.setPassword(resultSet.getString("password"));
                cliente.setSaldo(resultSet.getDouble("saldo"));
                return cliente;
            }
        }
        return null;
    }

    public Cliente obtenerClientePorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Clientes WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getInt("id"));
                cliente.setNombre(resultSet.getString("nombre"));
                cliente.setApellido(resultSet.getString("apellido"));
                cliente.setEmail(resultSet.getString("email"));
                cliente.setPassword(resultSet.getString("password"));
                cliente.setSaldo(resultSet.getDouble("saldo"));
                return cliente;
            }
        }
        return null;
    }

    public Cliente obtenerClientePorId(int id) throws SQLException {
        String sql = "SELECT * FROM Clientes WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getInt("id"));
                cliente.setNombre(resultSet.getString("nombre"));
                cliente.setApellido(resultSet.getString("apellido"));
                cliente.setEmail(resultSet.getString("email"));
                cliente.setPassword(resultSet.getString("password"));
                cliente.setSaldo(resultSet.getDouble("saldo"));
                return cliente;
            }
        }
        return null;
    }

    public double obtenerSaldo(int clienteId) throws SQLException {
        String sql = "SELECT saldo FROM Clientes WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clienteId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("saldo");
            }
        }
        return 0.0;
    }

    public void actualizarSaldo(Cliente cliente) throws SQLException {
        String sql = "UPDATE Clientes SET saldo = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, cliente.getSaldo());
            statement.setInt(2, cliente.getId());
            statement.executeUpdate();
        }
    }
}
