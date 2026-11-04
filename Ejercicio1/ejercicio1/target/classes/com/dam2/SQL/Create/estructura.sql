CREATE TABLE DEPARTAMENTO(
    id_departamento INTEGER NOT NULL PRIMARY KEY,
    nombre VARCHAR,
    ubicacion VARCHAR
);

CREATE TABLE CLIENTE(
    id_cliente INTEGER NOT NULL PRIMARY KEY,
    nombre VARCHAR,
    persona_contacto VARCHAR,
    tlfno INT,
    CIF INT
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