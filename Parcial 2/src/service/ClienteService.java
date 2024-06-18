package service;

import dao.ClienteDAO;
import dao.TransaccionDAO;
import model.Cliente;
import model.Transaccion;

import java.sql.SQLException;

public class ClienteService {
    private ClienteDAO clienteDAO;
    private TransaccionDAO transaccionDAO;

    public ClienteService(ClienteDAO clienteDAO, TransaccionDAO transaccionDAO) {
        this.clienteDAO = clienteDAO;
        this.transaccionDAO = transaccionDAO;
    }

    public void registrarCliente(Cliente cliente) throws SQLException {
        clienteDAO.crearCliente(cliente);
    }

    public Cliente login(String email, String password) throws SQLException {
        Cliente cliente = clienteDAO.obtenerClientePorEmail(email);
        if (cliente != null && cliente.getPassword().equals(password)) {
            return cliente;
        }
        return null;
    }

    public double revisarSaldo(int clienteId) throws SQLException {
        Cliente cliente = clienteDAO.obtenerClientePorId(clienteId);
        return cliente.getSaldo();
    }

    public void depositar(int clienteId, double monto) throws SQLException {
        Cliente cliente = clienteDAO.obtenerClientePorId(clienteId);
        cliente.setSaldo(cliente.getSaldo() + monto);
        clienteDAO.actualizarSaldo(cliente);

        Transaccion transaccion = new Transaccion();
        transaccion.setClienteId(clienteId);
        transaccion.setTipo("Deposito");
        transaccion.setMonto(monto);
        transaccionDAO.registrarTransaccion(transaccion);
    }

    public void retirar(int clienteId, double monto) throws SQLException {
        Cliente cliente = clienteDAO.obtenerClientePorId(clienteId);
        if (cliente.getSaldo() >= monto) {
            cliente.setSaldo(cliente.getSaldo() - monto);
            clienteDAO.actualizarSaldo(cliente);

            Transaccion transaccion = new Transaccion();
            transaccion.setClienteId(clienteId);
            transaccion.setTipo("Retiro");
            transaccion.setMonto(monto);
            transaccionDAO.registrarTransaccion(transaccion);
        } else {
            throw new SQLException("Saldo insuficiente");
        }
    }

    public void transferir(int clienteOrigenId, int clienteDestinoId, double monto) throws SQLException {
        Cliente clienteOrigen = clienteDAO.obtenerClientePorId(clienteOrigenId);
        Cliente clienteDestino = clienteDAO.obtenerClientePorId(clienteDestinoId);

        if (clienteOrigen.getSaldo() >= monto) {
            clienteOrigen.setSaldo(clienteOrigen.getSaldo() - monto);
            clienteDestino.setSaldo(clienteDestino.getSaldo() + monto);
            clienteDAO.actualizarSaldo(clienteOrigen);
            clienteDAO.actualizarSaldo(clienteDestino);

            Transaccion transaccionOrigen = new Transaccion();
            transaccionOrigen.setClienteId(clienteOrigenId);
            transaccionOrigen.setTipo("Transferencia Enviada");
            transaccionOrigen.setMonto(monto);
            transaccionDAO.registrarTransaccion(transaccionOrigen);

            Transaccion transaccionDestino = new Transaccion();
            transaccionDestino.setClienteId(clienteDestinoId);
            transaccionDestino.setTipo("Transferencia Recibida");
            transaccionDestino.setMonto(monto);
            transaccionDAO.registrarTransaccion(transaccionDestino);
        } else {
            throw new SQLException("Saldo insuficiente");
        }
    }
}

