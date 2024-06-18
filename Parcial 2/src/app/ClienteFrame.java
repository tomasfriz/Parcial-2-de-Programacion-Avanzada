package app;

import exception.CajeroException;
import model.Cliente;
import service.ClienteService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ClienteFrame extends JFrame {
    private Cliente cliente;
    private ClienteService clienteService;
    private JLabel saldoLabel;

    public ClienteFrame(Cliente cliente, ClienteService clienteService) {
        this.cliente = cliente;
        this.clienteService = clienteService;

        setTitle("Cajero Automático - Cliente");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        saldoLabel = new JLabel("Saldo: " + cliente.getSaldo());
        JButton depositarButton = new JButton("Depositar");
        JButton retirarButton = new JButton("Retirar");
        JButton transferirButton = new JButton("Transferir");
        JButton revisarSaldoButton = new JButton("Revisar Saldo");
        JButton cerrarSesionButton = new JButton("Cerrar Sesión");

        depositarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDepositar();
            }
        });

        retirarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRetirar();
            }
        });

        transferirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleTransferir();
            }
        });

        revisarSaldoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRevisarSaldo();
            }
        });

        cerrarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCerrarSesion();
            }
        });

        panel.add(saldoLabel);
        panel.add(new JLabel());
        panel.add(depositarButton);
        panel.add(retirarButton);
        panel.add(transferirButton);
        panel.add(revisarSaldoButton);
        panel.add(cerrarSesionButton);

        add(panel);
    }

    private void handleDepositar() {
        String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a depositar:");
        if (montoStr != null) {
            try {
                double monto = Double.parseDouble(montoStr);
                clienteService.depositar(cliente.getId(), monto);
                cliente.setSaldo(cliente.getSaldo() + monto);
                saldoLabel.setText("Saldo: " + cliente.getSaldo());
            } catch (SQLException | CajeroException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRetirar() {
        String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a retirar:");
        if (montoStr != null) {
            try {
                double monto = Double.parseDouble(montoStr);
                clienteService.retirar(cliente.getId(), monto);
                cliente.setSaldo(cliente.getSaldo() - monto);
                saldoLabel.setText("Saldo: " + cliente.getSaldo());
            } catch (SQLException | CajeroException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                cliente.setSaldo(cliente.getSaldo() - monto);
                saldoLabel.setText("Saldo: " + cliente.getSaldo());
            } catch (SQLException | CajeroException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRevisarSaldo() {
        try {
            double saldo = clienteService.revisarSaldo(cliente.getId());
            saldoLabel.setText("Saldo: " + saldo);
        } catch (SQLException | CajeroException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
