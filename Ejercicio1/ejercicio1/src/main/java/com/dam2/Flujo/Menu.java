package com.dam2.Flujo;

import java.sql.Connection;
import java.util.Scanner;

import com.dam2.SQL.sentenciasSQL;

public class Menu {

    static Connection con = sentenciasSQL.conectarBDD();

    static int opcion;
    static String tabla;
    static String[] valores;

    static Scanner sc = new Scanner(System.in);

    public static void menu() throws Exception {
        System.out.println("Bienvenido a la bdd de la actividad 1");

        boolean salir = false;
        while (!salir) {
            System.out.println("Selecciona una opcion: ");
            System.out.println("1. Introducir a la BDD");
            System.out.println("2. Eliminar de la BDD");
            System.out.println("3. Editar de la BDD");

            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    System.out.println("Ha seleccionado introducir a la BDD");
                    System.out.println("Indique la tabla");
                    System.out.println("1. Departamento");
                    System.out.println("2. Empleado");
                    System.out.println("3. Cliente");
                    System.out.println("4. Proyecto");

                    opcion = sc.nextInt();
                    String[] columnas;
                    Object[] valores;

                    int id_departamento, id_proyecto, id_cliente, id_empleado, telefono, cif;
                    String nombre, apellidos, ubicacion, dni, personaContacto;
                    Float salario;

                    switch (opcion) {
                        case 1:

                            System.out.println("--------------TABLAS--------------");
                            System.out.println("[ID][NOMBRE][UBICACION]");
                            System.out.print("id: ");
                            id_departamento = sc.nextInt();
                            System.out.print("nombre: ");
                            nombre = sc.next();
                            System.out.print("ubicacion: ");
                            ubicacion = sc.next();

                            tabla = "Departamento";
                            columnas = new String[] { "id_departamento", "nombre", "ubicacion" };
                            valores = new Object[] { id_departamento, nombre, ubicacion };

                            break;

                        case 2:
                            System.out.println("--------------TABLAS--------------");
                            System.out.println(
                                    "[ID_EMPLEADO][ID_DEPARTAMENTO][ID_PROYECTO][NOMBRE][APELLIDOS][DNI][SALARIO]");
                            // id_empleado
                            System.out.print("id: ");
                            id_empleado = sc.nextInt();
                            // id_departament
                            System.out.print("id: ");
                            id_departamento = sc.nextInt();
                            // id_proyecto
                            System.out.print("id: ");
                            id_proyecto = sc.nextInt();
                            // nombre
                            System.out.print("nombre: ");
                            nombre = sc.next();
                            // ubicacion
                            System.out.print("apellidos: ");
                            apellidos = sc.next();
                            // DNI
                            System.out.print("DNI: ");
                            dni = sc.next();
                            // Salario
                            System.out.print("salario: ");
                            salario = sc.nextFloat();

                            tabla = "Empleado";
                            columnas = new String[] { "id_empleado", "id_departamento", "id_proyecto", "nombre",
                                    "apellidos", "DNI", "salario" };
                            valores = new Object[] { id_empleado, id_departamento, id_proyecto, nombre, apellidos, dni, salario };
                            break;

                        case 3:
                            System.out.println("--------------TABLAS--------------");
                            System.out.println(
                                    "[ID_CLIENTE][NOMBRE][PERSONA DE CONTACTO][TELEFONO][CIF]");
                            // id_empleado
                            System.out.print("id: ");
                            id_empleado = sc.nextInt();
                            // nombre
                            System.out.print("nombre: ");
                            nombre = sc.next();
                            // persona contacto
                            System.out.print("persona contacto: ");
                            personaContacto = sc.next();
                            // telefono
                            System.out.print("telefono: ");
                            telefono = sc.nextInt();
                            // CIF, Numero de identificacion fiscal
                            System.out.print("CIF: ");
                            cif = sc.nextInt();

                            tabla = "Cliente";
                            columnas = new String[] { "id_cliente", "nombre", "persona_contacto", "tlfno", "CIF" };
                            valores = new Object[] { id_empleado, nombre, personaContacto, telefono, cif };
                            break;
                        case 4:
                            System.out.println("--------------TABLAS--------------");
                            System.out.println(
                                    "[ID_CLIENTE][NOMBRE][PERSONA DE CONTACTO][TELEFONO][CIF]");
                            // id_proyecto
                            System.out.print("id: ");
                            id_proyecto = sc.nextInt();
                            // nombre
                            System.out.print("nombre: ");
                            nombre = sc.next();
                            // ubicacion
                            System.out.print("ubicacion: ");
                            ubicacion = sc.next();
                            // id_cliente
                            System.out.print("id_cliente: ");
                            id_cliente = sc.nextInt();

                            tabla = "Proyecto";
                            columnas = new String[] { "id_proyecto", "nombre", "ubicacion", "id_cliente" };
                            valores = new Object[] { id_proyecto, nombre, ubicacion, id_cliente };
                            break;

                        default:
                            System.out.println("Opcion no valida");
                            break;
                    }
                    
                    sentenciasSQL.insertarDatos(con, tabla, columnas, valores);
                    break;

                default:
                    break;
            }
        }

    }

}
