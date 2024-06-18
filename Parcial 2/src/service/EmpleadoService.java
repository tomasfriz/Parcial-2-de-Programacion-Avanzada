package service;

import dao.EmpleadoDAO;
import dao.TransaccionDAO;
import exception.CajeroException;
import model.Empleado;
import model.Transaccion;

import java.sql.SQLException;
import java.util.List;

public class EmpleadoService {
    private EmpleadoDAO empleadoDAO;
    private TransaccionDAO transaccionDAO;

    public EmpleadoService(EmpleadoDAO empleadoDAO, TransaccionDAO transaccionDAO) {
        this.empleadoDAO = empleadoDAO;
        this.transaccionDAO = transaccionDAO;
    }

    public Empleado login(String email, String password) throws SQLException, CajeroException {
        Empleado empleado = empleadoDAO.obtenerEmpleadoPorEmail(email);
        if (empleado != null && empleado.getPassword().equals(password)) {
            return empleado;
        }
        throw new CajeroException("Email o contraseña incorrectos");
    }

    public List<Transaccion> obtenerTodasLasTransacciones() throws SQLException {
        return transaccionDAO.obtenerTodasLasTransacciones();
    }
}
