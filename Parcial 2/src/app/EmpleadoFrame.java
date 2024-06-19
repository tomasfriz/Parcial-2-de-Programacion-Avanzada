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
    private JLabel cantidadDineroLabel;

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

        cantidadDineroLabel = new JLabel();
        actualizarCantidadDineroLabel();

        JButton verTransaccionesButton = new JButton("Ver Todas las Transacciones");
        verTransaccionesButton.addActionListener(e -> handleVerTransacciones());

        JButton reponerDineroButton = new JButton("Reponer Dinero");
        reponerDineroButton.addActionListener(e -> handleReponerDinero());

        JButton cerrarSesionButton = new JButton("Cerrar Sesión");
        cerrarSesionButton.addActionListener(e -> handleCerrarSesion());

        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new GridLayout(1, 3));
        botonesPanel.add(verTransaccionesButton);
        botonesPanel.add(reponerDineroButton);
        botonesPanel.add(cerrarSesionButton);

        panel.add(cantidadDineroLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(botonesPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void actualizarCantidadDineroLabel() {
        try {
            double cantidadDinero = empleadoService.obtenerCantidadDinero();
            cantidadDineroLabel.setText("Cantidad de Dinero en el Cajero: $" + cantidadDinero);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener la cantidad de dinero del cajero", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
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

    private void handleReponerDinero() {
        String cantidadStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad de dinero a reponer:");
        if (cantidadStr != null) {
            try {
                double cantidad = Double.parseDouble(cantidadStr);
                empleadoService.reponerDinero(cantidad);
                actualizarCantidadDineroLabel();
                JOptionPane.showMessageDialog(this, "Dinero repuesto exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al reponer el dinero", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad válida", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
