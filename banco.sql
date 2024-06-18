-- Crear base de datos
CREATE DATABASE Banco;

-- Seleccionar base de datos
USE Banco;

-- Crear tabla de clientes
CREATE TABLE Clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(256),
    saldo DECIMAL(10, 2)
);

-- Crear tabla de transacciones
CREATE TABLE Transacciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    tipo VARCHAR(50),
    monto DECIMAL(10, 2),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES Clientes(id)
);

-- Crear tabla de empleados
CREATE TABLE Empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(256)
);

-- Insertar datos de ejemplo en la tabla de clientes
INSERT INTO Clientes (nombre, apellido, email, password, saldo) VALUES
('Belen', 'Perez', 'belen.perez@gmail.com', 'user123', 1000.00),
('Miguel', 'Gomez', 'miguel.gomez@gmail.com', 'user123', 1500.00);

-- Insertar datos de ejemplo en la tabla de transacciones
INSERT INTO Transacciones (cliente_id, tipo, monto) VALUES
(1, 'Deposito', 500.00),
(1, 'Retiro', 200.00),
(2, 'Deposito', 300.00);

-- Insertar datos de ejemplo en la tabla de empleados
INSERT INTO Empleados (nombre, apellido, email, password) VALUES
('Tomas', 'Friz', 'tomas.friz@gmail.com', 'admin123'),
('Gabriela', 'Diaz', 'gabriela.diaz@gmail.com', 'admin123');
