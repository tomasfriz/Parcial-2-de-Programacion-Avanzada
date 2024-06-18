package dao;

import model.Cliente;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {

    public void crearCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Clientes (nombre, apellido, email, password, saldo) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getApellido());
            statement.setString(3, cliente.getEmail());
            statement.setString(4, cliente.getPassword());
            statement.setDouble(5, cliente.getSaldo());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al crear el cliente", e);
        }
    }

    public Cliente obtenerClientePorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Clientes WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
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
        } catch (SQLException e) {
            throw new SQLException("Error al obtener el cliente por email", e);
        }
        return null;
    }

    public Cliente obtenerClientePorId(int id) throws SQLException {
        String sql = "SELECT * FROM Clientes WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
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
        } catch (SQLException e) {
            throw new SQLException("Error al obtener el cliente por ID", e);
        }
        return null;
    }

    public void actualizarSaldo(Cliente cliente) throws SQLException {
        String sql = "UPDATE Clientes SET saldo = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, cliente.getSaldo());
            statement.setInt(2, cliente.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el saldo del cliente", e);
        }
    }
}
