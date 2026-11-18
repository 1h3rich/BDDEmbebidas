CONNECT 'jdbc:derby:ejercicio2/assets/Actividad2;create=true';

CREATE TABLE PROFESOR (
    dni VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    fecha_nacimiento DATE,
    codigo_postal VARCHAR(10),
    telefono VARCHAR(15)
);


CREATE TABLE ALUMNO (
    dni VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    fecha_nacimiento DATE,
    codigo_postal VARCHAR(10),
    telefono VARCHAR(15)
);

CREATE TABLE CURSO (
    codigo_curso INTEGER PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);
CREATE TABLE AULA (
    codigo_aula INTEGER PRIMARY KEY,
    piso INTEGER,
    numero_pupitres INTEGER
);

CREATE TABLE ASIGNATURA (
    codigo_asignatura INTEGER PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    horas_semanales INTEGER NOT NULL,

    dni_profesor VARCHAR(15) NOT NULL,
    codigo_curso INTEGER NOT NULL,

    FOREIGN KEY (dni_profesor)
        REFERENCES PROFESOR(dni),

    FOREIGN KEY (codigo_curso)
        REFERENCES CURSO(codigo_curso)
);

CREATE TABLE MATRICULA (
    dni_alumno VARCHAR(15),
    codigo_asignatura INTEGER,

    calificacion DECIMAL(4,2),

    PRIMARY KEY (dni_alumno, codigo_asignatura),

    FOREIGN KEY (dni_alumno)
        REFERENCES ALUMNO(dni),

    FOREIGN KEY (codigo_asignatura)
        REFERENCES ASIGNATURA(codigo_asignatura)
);

CREATE TABLE INCIDENCIA (
    id_incidencia INTEGER PRIMARY KEY,
    dni_alumno VARCHAR(15) NOT NULL,
    codigo_asignatura INTEGER NOT NULL,

    descripcion VARCHAR(300),
    fecha DATE,

    FOREIGN KEY (dni_alumno, codigo_asignatura)
        REFERENCES MATRICULA(dni_alumno, codigo_asignatura)
);

CREATE TABLE HORARIO (
    codigo_asignatura INTEGER,
    codigo_aula INTEGER,

    mes INTEGER,
    dia INTEGER,
    hora INTEGER,

    PRIMARY KEY (
        codigo_asignatura,
        codigo_aula,
        mes,
        dia,
        hora
    ),

    FOREIGN KEY (codigo_asignatura)
        REFERENCES ASIGNATURA(codigo_asignatura),

    FOREIGN KEY (codigo_aula)
        REFERENCES AULA(codigo_aula)
);

-- Generado con IA (Los Inserts)


-- =========================================
-- 1. PROFESORES
-- =========================================

INSERT INTO PROFESOR VALUES
('11111111A', 'Carlos', 'García López', 'Calle Mayor 12', '1980-05-14', '50001', '611111111'),
('22222222B', 'María', 'Fernández Ruiz', 'Avenida Aragón 25', '1975-08-21', '50002', '622222222'),
('33333333C', 'Pedro', 'Martínez Sánchez', 'Calle Luna 8', '1985-02-10', '50003', '633333333'),
('44444444D', 'Laura', 'Gómez Pérez', 'Calle Sol 14', '1990-11-30', '50004', '644444444'),
('55555555E', 'Antonio', 'Rodríguez Díaz', 'Plaza España 3', '1978-04-18', '50005', '655555555');


-- =========================================
-- 2. ALUMNOS
-- =========================================

INSERT INTO ALUMNO VALUES
('66666666F', 'Juan', 'Pérez García', 'Calle Río 1', '2002-03-15', '50001', '666111111'),
('77777777G', 'Ana', 'López Martín', 'Calle Sol 2', '2001-07-20', '50002', '666222222'),
('88888888H', 'Luis', 'Sánchez Ruiz', 'Avenida Central 5', '2003-01-12', '50003', '666333333'),
('99999999J', 'Elena', 'Martínez Gil', 'Calle Norte 7', '2002-09-25', '50004', '666444444'),
('10101010K', 'David', 'Romero Díaz', 'Calle Sur 9', '2000-12-01', '50005', '666555555'),
('12121212L', 'Lucía', 'Torres Pérez', 'Calle Este 11', '2001-04-17', '50006', '666666666'),
('13131313M', 'Mario', 'Navarro López', 'Calle Oeste 13', '2003-06-22', '50007', '666777777'),
('14141414N', 'Sofía', 'Díaz Fernández', 'Avenida Principal 15', '2002-02-08', '50008', '666888888'),
('15151515P', 'Pablo', 'Moreno Sánchez', 'Calle Jardín 17', '2001-10-19', '50009', '666999999'),
('16161616Q', 'Claudia', 'Ruiz Gómez', 'Calle Nueva 19', '2003-08-11', '50010', '666000000');


-- =========================================
-- 3. CURSOS
-- =========================================

INSERT INTO CURSO VALUES
(1, 'Desarrollo de Aplicaciones Web'),
(2, 'Administración de Sistemas Informáticos'),
(3, 'Marketing y Gestión Empresarial');


-- =========================================
-- 4. AULAS
-- =========================================

INSERT INTO AULA VALUES
(101, 1, 25),
(102, 1, 20),
(201, 2, 30),
(202, 2, 25),
(301, 3, 35);


-- =========================================
-- 5. ASIGNATURAS
-- =========================================

INSERT INTO ASIGNATURA VALUES
(1, 'Programación', 8, '11111111A', 1),
(2, 'Bases de Datos', 6, '11111111A', 1),
(3, 'Lenguajes de Marcas', 4, '22222222B', 1),
(4, 'Sistemas Informáticos', 6, '33333333C', 2),
(5, 'Redes', 5, '33333333C', 2),
(6, 'Administración de Sistemas', 7, '44444444D', 2),
(7, 'Marketing Digital', 5, '55555555E', 3),
(8, 'Gestión Empresarial', 4, '55555555E', 3);


-- =========================================
-- 6. MATRÍCULAS
-- =========================================

INSERT INTO MATRICULA VALUES
('66666666F', 1, 8.50),
('66666666F', 2, 9.00),
('66666666F', 3, 7.50),
('77777777G', 1, 6.50),
('77777777G', 2, 8.00),
('77777777G', 4, 7.00),
('88888888H', 1, 9.50),
('88888888H', 3, 8.50),
('88888888H', 5, 6.00),
('99999999J', 2, 5.50),
('99999999J', 4, 9.00),
('99999999J', 6, 8.50),
('10101010K', 4, 7.50),
('10101010K', 5, 8.00),
('10101010K', 6, 9.50),
('12121212L', 7, 8.50),
('12121212L', 8, 9.00),
('13131313M', 7, 6.50),
('13131313M', 8, 7.00),
('14141414N', 1, 4.50),
('14141414N', 2, 5.00),
('15151515P', 5, 8.50),
('15151515P', 6, 7.50),
('16161616Q', 3, 9.00),
('16161616Q', 8, 8.00);


-- =========================================
-- 7. INCIDENCIAS
-- =========================================

INSERT INTO INCIDENCIA VALUES
(1, '66666666F', 1, 'Retraso en la entrega de una práctica', '2026-01-15'),
(2, '77777777G', 1, 'Falta de asistencia', '2026-01-20'),
(3, '88888888H', 5, 'No ha entregado los ejercicios', '2026-02-03'),
(4, '99999999J', 2, 'Problemas de comportamiento', '2026-02-10'),
(5, '14141414N', 1, 'Necesita recuperar una evaluación', '2026-02-15'),
(6, '10101010K', 6, 'Retraso en varias entregas', '2026-03-01'),
(7, '13131313M', 7, 'Falta de asistencia', '2026-03-05');


-- =========================================
-- 8. HORARIOS
-- =========================================

INSERT INTO HORARIO VALUES
(1, 101, 1, 12, 9),
(1, 101, 1, 12, 10),
(1, 102, 1, 13, 9),
(2, 101, 1, 14, 11),
(2, 201, 1, 15, 12),
(3, 102, 1, 16, 9),
(3, 201, 1, 17, 10),
(4, 201, 2, 3, 9),
(4, 202, 2, 4, 10),
(5, 201, 2, 5, 11),
(5, 301, 2, 6, 12),
(6, 202, 2, 7, 9),
(7, 301, 3, 10, 10),
(7, 301, 3, 11, 11),
(8, 301, 3, 12, 12),
(8, 202, 3, 13, 9);


EXIT;