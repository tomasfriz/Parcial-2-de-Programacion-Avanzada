package app;

import model.Empleado;
import model.Transaccion;
import service.EmpleadoService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EmpleadoFrame extends JFrame {
    private Empleado empleado;
    private EmpleadoService empleadoService;
    private JTextArea transaccionesTextArea;

    public EmpleadoFrame(Empleado empleado, EmpleadoService empleadoService) {
        this.empleado = empleado;
        this.empleadoService = empleadoService;

        setTitle("Cajero Automático - Empleado");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        transaccionesTextArea = new JTextArea();
        transaccionesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transaccionesTextArea);

        JButton verTransaccionesButton = new JButton("Ver Todas las Transacciones");
        verTransaccionesButton.addActionListener(e -> handleVerTransacciones());

        JButton cerrarSesionButton = new JButton("Cerrar Sesión");
        cerrarSesionButton.addActionListener(e -> handleCerrarSesion());

        panel.add(verTransaccionesButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(cerrarSesionButton, BorderLayout.SOUTH);

        add(panel);
    }

    private void handleVerTransacciones() {
        try {
            List<Transaccion> transacciones = empleadoService.obtenerTodasLasTransacciones();
            transaccionesTextArea.setText("");
            for (Transaccion transaccion : transacciones) {
                transaccionesTextArea.append("ID: " + transaccion.getId() + ", Cliente ID: " + transaccion.getClienteId() +
                        ", Tipo: " + transaccion.getTipo() + ", Monto: " + transaccion.getMonto() + ", Fecha: " + transaccion.getFecha() + "\n");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener las transacciones", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleCerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea cerrar sesión?", "Confirmar Cierre de Sesión", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        }
    }
}
