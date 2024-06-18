package app;

import exception.CajeroException;
import model.Cliente;
import model.Empleado;
import service.ClienteService;
import service.EmpleadoService;
import dao.ClienteDAO;
import dao.EmpleadoDAO;
import dao.TransaccionDAO;
import dao.EstadoCajeroDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private ClienteService clienteService;
    private EmpleadoService empleadoService;

    public LoginFrame() {
        setTitle("Cajero Automático - Iniciar Sesión");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ClienteDAO clienteDAO = new ClienteDAO();
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();
        TransaccionDAO transaccionDAO = new TransaccionDAO();
        EstadoCajeroDAO estadoCajeroDAO = new EstadoCajeroDAO();

        clienteService = new ClienteService(clienteDAO, transaccionDAO);
        empleadoService = new EmpleadoService(empleadoDAO, transaccionDAO, estadoCajeroDAO);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField();
        JLabel userTypeLabel = new JLabel("Tipo de Usuario:");
        String[] userTypes = {"Cliente", "Empleado"};
        JComboBox<String> userTypeComboBox = new JComboBox<>(userTypes);

        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleLogin((String) userTypeComboBox.getSelectedItem());
                } catch (SQLException | CajeroException ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    limpiarCampos();
                }
            }
        });

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(userTypeLabel);
        panel.add(userTypeComboBox);
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);
    }

    private void handleLogin(String userType) throws SQLException, CajeroException {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (userType.equals("Cliente")) {
            Cliente cliente = clienteService.login(email, password);
            if (cliente != null) {
                ClienteFrame clienteFrame = new ClienteFrame(cliente, clienteService);
                clienteFrame.setVisible(true);
                dispose();
            } else {
                throw new CajeroException("Email o contraseña incorrectos");
            }
        } else if (userType.equals("Empleado")) {
            Empleado empleado = empleadoService.login(email, password);
            if (empleado != null) {
                EmpleadoFrame empleadoFrame = new EmpleadoFrame(empleado, empleadoService);
                empleadoFrame.setVisible(true);
                dispose();
            } else {
                throw new CajeroException("Email o contraseña incorrectos");
            }
        }
    }

    private void limpiarCampos() {
        emailField.setText("");
        passwordField.setText("");
    }
}
