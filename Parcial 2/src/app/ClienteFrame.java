package app;

import exception.CajeroException;
import model.Cliente;
import model.Transaccion;
import service.ClienteService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ClienteFrame extends JFrame {
    private Cliente cliente;
    private ClienteService clienteService;
    private JTextArea transaccionesTextArea;
    private JLabel saldoLabel;

    public ClienteFrame(Cliente cliente, ClienteService clienteService) {
        this.cliente = cliente;
        this.clienteService = clienteService;

        setTitle("Cajero Automático - Cliente");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        transaccionesTextArea = new JTextArea();
        transaccionesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transaccionesTextArea);

        saldoLabel = new JLabel();
        actualizarSaldoLabel();

        JButton verMovimientosButton = new JButton("Ver Movimientos de Cuenta");
        verMovimientosButton.addActionListener(e -> handleVerMovimientos());

        JButton verSaldoButton = new JButton("Ver Saldo");
        verSaldoButton.addActionListener(e -> handleVerSaldo());

        JButton depositarButton = new JButton("Depositar");
        depositarButton.addActionListener(e -> handleDepositar());

        JButton retirarButton = new JButton("Retirar");
        retirarButton.addActionListener(e -> handleRetirar());

        JButton transferirButton = new JButton("Transferir");
        transferirButton.addActionListener(e -> handleTransferir());

        JButton cerrarSesionButton = new JButton("Cerrar Sesión");
        cerrarSesionButton.addActionListener(e -> handleCerrarSesion());

        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new GridLayout(2, 3));
        botonesPanel.add(verMovimientosButton);
        botonesPanel.add(verSaldoButton);
        botonesPanel.add(depositarButton);
        botonesPanel.add(retirarButton);
        botonesPanel.add(transferirButton);
        botonesPanel.add(cerrarSesionButton);

        panel.add(saldoLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(botonesPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void actualizarSaldoLabel() {
        try {
            double saldo = clienteService.obtenerSaldo(cliente.getId());
            saldoLabel.setText("Saldo actual: " + saldo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener el saldo del cliente", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleVerMovimientos() {
        try {
            List<Transaccion> transacciones = clienteService.obtenerMovimientos(cliente.getId());
            transaccionesTextArea.setText("");
            for (Transaccion transaccion : transacciones) {
                transaccionesTextArea.append("ID: " + transaccion.getId() + ", Tipo: " + transaccion.getTipo() +
                        ", Monto: " + transaccion.getMonto() + ", Fecha: " + transaccion.getFecha() + "\n");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener los movimientos de cuenta", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleVerSaldo() {
        actualizarSaldoLabel();
    }

    private void handleDepositar() {
        String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a depositar:");
        if (montoStr != null) {
            try {
                double monto = Double.parseDouble(montoStr);
                clienteService.depositar(cliente.getId(), monto);
                actualizarSaldoLabel();
                JOptionPane.showMessageDialog(this, "Depósito realizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al realizar el depósito", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad válida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRetirar() {
        String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a retirar:");
        if (montoStr != null) {
            try {
                double monto = Double.parseDouble(montoStr);
                clienteService.retirar(cliente.getId(), monto);
                actualizarSaldoLabel();
                JOptionPane.showMessageDialog(this, "Retiro realizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException | CajeroException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad válida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleTransferir() {
        String emailDestino = JOptionPane.showInputDialog(this, "Ingrese el email del destinatario:");
        String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a transferir:");
        if (emailDestino != null && montoStr != null) {
            try {
                Cliente destinatario = clienteService.login(emailDestino, ""); // Solo para obtener el cliente por email
                double monto = Double.parseDouble(montoStr);
                clienteService.transferir(cliente.getId(), destinatario.getId(), monto);
                actualizarSaldoLabel();
                JOptionPane.showMessageDialog(this, "Transferencia realizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException | CajeroException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
