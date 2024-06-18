package test;

import dao.InMemoryClienteDAO;
import dao.InMemoryTransaccionDAO;
import exception.CajeroException;
import model.Cliente;
import org.junit.Before;
import org.junit.Test;
import service.ClienteService;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ClienteServiceTest {

    private ClienteService clienteService;
    private InMemoryClienteDAO clienteDAO;
    private InMemoryTransaccionDAO transaccionDAO;

    @Before
    public void setUp() {
        clienteDAO = new InMemoryClienteDAO();
        transaccionDAO = new InMemoryTransaccionDAO();
        clienteService = new ClienteService(clienteDAO, transaccionDAO);

        Cliente cliente1 = new Cliente();
        cliente1.setNombre("John");
        cliente1.setApellido("Wick");
        cliente1.setEmail("john.wick@gmail.com");
        cliente1.setPassword("user123");
        cliente1.setSaldo(1000);
        clienteDAO.agregarCliente(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setNombre("Dimitri");
        cliente2.setApellido("Vegas");
        cliente2.setEmail("dimitri.vegas@gmail.com");
        cliente2.setPassword("user123");
        cliente2.setSaldo(500);
        clienteDAO.agregarCliente(cliente2);
    }

    @Test
    public void testDepositar() throws SQLException {
        Cliente cliente = clienteDAO.obtenerClientePorId(1);

        clienteService.depositar(1, 500);

        assertEquals(1500, cliente.getSaldo(), 0);
    }

    @Test
    public void testRetirar() throws SQLException, CajeroException {
        Cliente cliente = clienteDAO.obtenerClientePorId(1);

        clienteService.retirar(1, 500);

        assertEquals(500, cliente.getSaldo(), 0);
    }

    @Test
    public void testRetirarSaldoInsuficiente() throws SQLException, CajeroException {
        Cliente cliente = clienteDAO.obtenerClientePorId(1);

        CajeroException thrown = assertThrows(CajeroException.class, () -> {
            clienteService.retirar(1, 1500);
        });

        assertEquals("Saldo insuficiente", thrown.getMessage());
    }

    @Test
    public void testTransferir() throws SQLException, CajeroException {
        Cliente clienteOrigen = clienteDAO.obtenerClientePorId(1);
        Cliente clienteDestino = clienteDAO.obtenerClientePorId(2);

        clienteService.transferir(1, 2, 500);

        assertEquals(500, clienteOrigen.getSaldo(), 0);
        assertEquals(1000, clienteDestino.getSaldo(), 0);
    }

    @Test
    public void testTransferirSaldoInsuficiente() throws SQLException, CajeroException {
        Cliente clienteOrigen = clienteDAO.obtenerClientePorId(1);
        Cliente clienteDestino = clienteDAO.obtenerClientePorId(2);

        CajeroException thrown = assertThrows(CajeroException.class, () -> {
            clienteService.transferir(1, 2, 1500);
        });

        assertEquals("Saldo insuficiente", thrown.getMessage());
    }
}
