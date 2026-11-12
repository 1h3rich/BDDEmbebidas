CREATE TABLE DEPARTAMENTO(
    id_departamento INTEGER NOT NULL PRIMARY KEY,
    nombre VARCHAR,
    ubicacion VARCHAR
);

CREATE TABLE CLIENTE(
    id_cliente INTEGER NOT NULL PRIMARY KEY,
    nombre VARCHAR,
    persona_contacto VARCHAR,
    tlfno VARCHAR
    ,
    CIF VARCHAR
);

CREATE TABLE EMPLEADO(
    id_empleado INTEGER NOT NULL PRIMARY KEY,
    id_departamento INTEGER REFERENCES DEPARTAMENTO(id_departamento),
    id_proyecto INTEGER REFERENCES PROYECTO(id_proyecto),
    nombre VARCHAR,
    apellido VARCHAR,
    dni varchar,
    salario FLOAT
);

CREATE table PROYECTO(
    id_proyecto INTEGER NOT NULL PRIMARY KEY,
    nombre VARCHAR,
    ubicacion VARCHAR,
    id_cliente INTEGER REFERENCES CLIENTE(id_cliente)
);

-- Generado con IA (Los Inserts)

-- =====================================
-- DEPARTAMENTO
-- =====================================

INSERT INTO DEPARTAMENTO VALUES
(1, 'Informática', 'Madrid'),
(2, 'Recursos Humanos', 'Barcelona'),
(3, 'Contabilidad', 'Valencia'),
(4, 'Marketing', 'Sevilla'),
(5, 'Desarrollo', 'Zaragoza');


-- =====================================
-- CLIENTE
-- =====================================

INSERT INTO CLIENTE VALUES
(1, 'Google', 'Carlos Pérez', 612345678, 12345678),
(2, 'Microsoft', 'Ana García', 623456789, 23456789),
(3, 'Amazon', 'Luis Martín', 634567890, 34567890),
(4, 'Apple', 'María López', 645678901, 45678901),
(5, 'Samsung', 'Pedro Sánchez', 656789012, 56789012);


-- =====================================
-- PROYECTO
-- =====================================

INSERT INTO PROYECTO VALUES
(1, 'Desarrollo Web', 'Madrid', 1),
(2, 'Aplicación Móvil', 'Barcelona', 2),
(3, 'Sistema de Gestión', 'Valencia', 3),
(4, 'Tienda Online', 'Sevilla', 4),
(5, 'Inteligencia Artificial', 'Zaragoza', 5),
(6, 'Base de Datos', 'Madrid', 1);


-- =====================================
-- EMPLEADO
-- =====================================

INSERT INTO EMPLEADO VALUES
(1, 1, 1, 'Juan', 'García', '12345678A', 25000),
(2, 1, 2, 'Laura', 'Martínez', '23456789B', 28000),
(3, 2, 3, 'Pedro', 'López', '34567890C', 22000),
(4, 3, 4, 'Ana', 'Sánchez', '45678901D', 26000),
(5, 4, 5, 'Carlos', 'Fernández', '56789012E', 30000),
(6, 5, 6, 'Lucía', 'Pérez', '67890123F', 32000),
(7, 1, 1, 'David', 'Romero', '78901234G', 27000),
(8, 2, 2, 'Elena', 'Torres', '89012345H', 24000),
(9, 3, 3, 'Mario', 'Díaz', '90123456J', 29000),
(10, 5, 5, 'Sofía', 'Ruiz', '01234567K', 35000);

