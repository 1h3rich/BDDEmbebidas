CREATE DATABASE IF NOT EXISTS tienda_videojuegos;
USE tienda_videojuegos;

CREATE TABLE IF NOT EXISTS videojuego (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(120) NOT NULL,
    plataforma VARCHAR(60) NOT NULL,
    genero VARCHAR(60) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    apellidos VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS venta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_videojuego INT NOT NULL,
    fecha_venta DATE NOT NULL,
    unidades INT NOT NULL,
    importe_total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_venta_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id),
    CONSTRAINT fk_venta_videojuego FOREIGN KEY (id_videojuego) REFERENCES videojuego(id)
);

DROP PROCEDURE IF EXISTS consultar_ventas_cliente;
DROP PROCEDURE IF EXISTS total_gastado_cliente;

DELIMITER $$

CREATE PROCEDURE consultar_ventas_cliente(IN p_id_cliente INT)
BEGIN
    SELECT v.id AS id_venta,
           j.titulo AS nombre_videojuego,
           v.fecha_venta,
           v.unidades,
           v.importe_total AS importe
    FROM venta v
    INNER JOIN videojuego j ON j.id = v.id_videojuego
    WHERE v.id_cliente = p_id_cliente
    ORDER BY v.fecha_venta, v.id;
END$$

CREATE PROCEDURE total_gastado_cliente(IN p_id_cliente INT, OUT p_total DECIMAL(12,2))
BEGIN
    SELECT COALESCE(SUM(importe_total), 0)
    INTO p_total
    FROM venta
    WHERE id_cliente = p_id_cliente;
END$$

DELIMITER ;
