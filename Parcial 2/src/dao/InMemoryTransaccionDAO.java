package dao;

import model.Transaccion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTransaccionDAO extends TransaccionDAO {
    private List<Transaccion> transacciones = new ArrayList<>();
    private int currentId = 1;

    @Override
    public void registrarTransaccion(Transaccion transaccion) {
        transaccion.setId(currentId++);
        transacciones.add(transaccion);
    }

    @Override
    public List<Transaccion> obtenerMovimientosPorCliente(int clienteId) {
        return transacciones.stream()
                .filter(transaccion -> transaccion.getClienteId() == clienteId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaccion> obtenerTodasLasTransacciones() {
        return new ArrayList<>(transacciones);
    }
}
