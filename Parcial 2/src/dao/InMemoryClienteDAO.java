package dao;

import model.Cliente;

import java.util.HashMap;
import java.util.Map;

public class InMemoryClienteDAO extends ClienteDAO {
    private Map<Integer, Cliente> clientes = new HashMap<>();
    private int currentId = 1;

    @Override
    public Cliente obtenerClientePorEmail(String email, String password) {
        return clientes.values().stream()
                .filter(cliente -> cliente.getEmail().equals(email) && cliente.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Cliente obtenerClientePorId(int id) {
        return clientes.get(id);
    }

    @Override
    public double obtenerSaldo(int clienteId) {
        Cliente cliente = clientes.get(clienteId);
        return cliente != null ? cliente.getSaldo() : 0.0;
    }

    @Override
    public void actualizarSaldo(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
    }

    public void agregarCliente(Cliente cliente) {
        cliente.setId(currentId++);
        clientes.put(cliente.getId(), cliente);
    }
}
