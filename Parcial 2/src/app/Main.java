package app;

import model.Cliente;
import service.ClienteService;
import dao.ClienteDAO;
import dao.TransaccionDAO;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ClienteService clienteService;

    public static void main(String[] args) {
        ClienteDAO clienteDAO = new ClienteDAO();
        TransaccionDAO transaccionDAO = new TransaccionDAO();
        clienteService = new ClienteService(clienteDAO, transaccionDAO);

        while (true) {
            mostrarMenuPrincipal();
            int opcion = scanner.nextInt();
            scanner.nextLine();  // Consumir el salto de línea
            switch (opcion) {
                case 1:
                    iniciarSesion();
                    break;
                case 2:
                    registrarCliente();
                    break;
                case 3:
                    System.out.println("Gracias por usar el cajero automático. Adiós.");
                    return;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("Bienvenido al Cajero Automático");
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Registrar Cliente");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void iniciarSesion() {
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String password = scanner.nextLine();
        try {
            Cliente cliente = clienteService.login(email, password);
            if (cliente != null) {
                System.out.println("Bienvenido, " + cliente.getNombre());
                // Mostrar menú de transacciones
            } else {
                System.out.println("Email o contraseña incorrectos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void registrarCliente() {
        System.out.print("Ingrese su nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese su apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String password = scanner.nextLine();
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setEmail(email);
        cliente.setPassword(password);
        cliente.setSaldo(0.0);
        try {
            clienteService.registrarCliente(cliente);
            System.out.println("Cliente registrado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
