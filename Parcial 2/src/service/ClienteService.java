package service;

import dao.ClienteDAO;
import dao.TransaccionDAO;
import exception.CajeroException;
import model.Cliente;
import model.Transaccion;

import java.sql.SQLException;
import java.util.List;

public class ClienteService {
    private ClienteDAO clienteDAO;
    private TransaccionDAO transaccionDAO;

    public ClienteService(ClienteDAO clienteDAO, TransaccionDAO transaccionDAO) {
        this.clienteDAO = clienteDAO;
        this.transaccionDAO = transaccionDAO;
    }

    public double obtenerSaldo(int clienteId) throws SQLException {
        Cliente cliente = clienteDAO.obtenerClientePorId(clienteId);
        if (cliente == null) {
            throw new SQLException("Cliente no encontrado");
        }
        return cliente.getSaldo();
    }

    public List<Transaccion> obtenerMovimientos(int clienteId) throws SQLException {
        return transaccionDAO.obtenerMovimientosPorCliente(clienteId);
    }

    public Cliente login(String email, String password) throws SQLException, CajeroException {
        Cliente cliente = clienteDAO.obtenerClientePorEmail(email, password);
        if (cliente == null) {
            throw new CajeroException("Email o contraseña incorrectos");
        }
        return cliente;
    }

    public void depositar(int clienteId, double monto) throws SQLException {
        Cliente cliente = clienteDAO.obtenerClientePorId(clienteId);
        if (cliente == null) {
            throw new SQLException("Cliente no encontrado");
        }
        cliente.setSaldo(cliente.getSaldo() + monto);
        clienteDAO.actualizarSaldo(cliente);

        Transaccion transaccion = new Transaccion();
        transaccion.setClienteId(clienteId);
        transaccion.setTipo("Deposito");
        transaccion.setMonto(monto);
        transaccionDAO.registrarTransaccion(transaccion);
    }

    public void retirar(int clienteId, double monto) throws SQLException, CajeroException {
        Cliente cliente = clienteDAO.obtenerClientePorId(clienteId);
        if (cliente == null) {
            throw new SQLException("Cliente no encontrado");
        }
        if (cliente.getSaldo() >= monto) {
            cliente.setSaldo(cliente.getSaldo() - monto);
            clienteDAO.actualizarSaldo(cliente);

            Transaccion transaccion = new Transaccion();
            transaccion.setClienteId(clienteId);
            transaccion.setTipo("Retiro");
            transaccion.setMonto(monto);
            transaccionDAO.registrarTransaccion(transaccion);
        } else {
            throw new CajeroException("Saldo insuficiente");
        }
    }

    public void transferir(int clienteOrigenId, int clienteDestinoId, double monto) throws SQLException, CajeroException {
        Cliente clienteOrigen = clienteDAO.obtenerClientePorId(clienteOrigenId);
        Cliente clienteDestino = clienteDAO.obtenerClientePorId(clienteDestinoId);

        if (clienteOrigen == null || clienteDestino == null) {
            throw new CajeroException("Cliente origen o destino no encontrado");
        }

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
            throw new CajeroException("Saldo insuficiente");
        }
    }
}
