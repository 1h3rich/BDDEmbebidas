package com.dam2.Flujo;

import java.sql.Connection;
import java.util.Scanner;
import com.dam2.SQL.Conexion.*;
import com.dam2.SQL.Delete.Delete;
import com.dam2.SQL.Edit.Edit;
import com.dam2.SQL.Inserts.*;
import com.dam2.SQL.Select.*;

public class Menu {

    static Connection con = OnOff.conectarBDD(); // Establecemos conexion.

    static int opcion;
    static String tabla;
    static String[] valores;

    static Scanner sc = new Scanner(System.in);

    public static void menu() {

        System.out.println(con.toString());

        System.out.println("Bienvenido a la bdd de la actividad 1");

        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("Selecciona una opcion: ");
            System.out.println("1. Introducir a la BDD");
            System.out.println("2. Eliminar de la BDD");
            System.out.println("3. Editar de la BDD");
            System.out.println("4. Consultar");

            opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> {
                    introducirBDD();
                }
                case 2 -> {
                    eliminarBDD();
                }
                case 3 -> {
                    editarBDD();
                }
                case 4 -> {
                    consulta();
                }
                default -> {
                    System.out.println("Opcion no disponible");
                    break;
                }
            }
        }

    }

    public static void introducirBDD() {
        System.out.println("Ha seleccionado introducir a la BDD");
        System.out.println("Indique la tabla");
        System.out.println("1. Departamento");
        System.out.println("2. Empleado");
        System.out.println("3. Cliente");
        System.out.println("4. Proyecto");

        opcion = sc.nextInt();
        String[] columnas;
        Object[] valores;

        int id_departamento, id_proyecto, id_cliente, id_empleado;
        String nombre, apellido, ubicacion, dni, persona_contacto, telefono, cif;
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
                Insert.insertarDatos(con, tabla, columnas, valores);

                break;

            case 2:
                System.out.println("--------------TABLAS--------------");
                System.out.println(
                        "[ID_EMPLEADO][ID_DEPARTAMENTO][ID_PROYECTO][NOMBRE][APELLIDOS][DNI][SALARIO]");
                // id_empleado
                System.out.print("id empleado: ");
                id_empleado = sc.nextInt();
                // id_departament
                System.out.print("id departamento: ");
                id_departamento = sc.nextInt();
                // id_proyecto
                System.out.print("id proyecto: ");
                id_proyecto = sc.nextInt();
                // nombre
                System.out.print("nombre: ");
                nombre = sc.next();
                // ubicacion
                System.out.print("apellidos: ");
                apellido = sc.next();
                // DNI
                System.out.print("DNI: ");
                dni = sc.next();
                // Salario
                System.out.print("salario: ");
                salario = sc.nextFloat();

                tabla = "Empleado";
                columnas = new String[] { "id_empleado", "id_departamento", "id_proyecto", "nombre",
                        "apellidos", "DNI", "salario" };
                valores = new Object[] { id_empleado, id_departamento, id_proyecto, nombre, apellido, dni, salario };
                Insert.insertarDatos(con, tabla, columnas, valores);
                break;

            case 3:
                System.out.println("--------------TABLAS--------------");
                System.out.println(
                        "[ID_CLIENTE][NOMBRE][PERSONA DE CONTACTO][TELEFONO][CIF]");
                // id_empleado
                System.out.print("id cliente: ");
                id_empleado = sc.nextInt();
                // nombre
                System.out.print("nombre: ");
                nombre = sc.next();
                // persona contacto
                System.out.print("persona contacto: ");
                persona_contacto = sc.next();
                // telefono
                System.out.print("telefono: ");
                telefono = sc.next();
                // CIF, Numero de identificacion fiscal
                System.out.print("CIF: ");
                cif = sc.next();

                tabla = "Cliente";
                columnas = new String[] { "id_cliente", "nombre", "persona_contacto", "tlfno", "CIF" };
                valores = new Object[] { id_empleado, nombre, persona_contacto, telefono, cif };
                Insert.insertarDatos(con, tabla, columnas, valores);
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
                Insert.insertarDatos(con, tabla, columnas, valores);
                break;

            default:
                System.out.println("Opcion no valida");
                break;
        }
    }

    public static void consulta() {
        System.out.println("Elije el tipo de consulta: ");
        System.out.println("1. Query básica");
        System.out.println("2. Busqueda por id");
        int opcion = sc.nextInt();
        switch (opcion) {
            case 1 -> {
                System.out.println("Indique el dato: ");
                String dato = sc.next();
                System.out.println("Indique la tabla");
                tabla = sc.next();
                Querys.Query1(con, tabla, dato);
            }
            case 2 -> {
                System.out.println("Indique el dato");
                String dato = sc.next();
                System.out.println("Indique la tabla");
                tabla = sc.next();
                System.out.println("Indique la id");
                int id = sc.nextInt();
                Querys.Query2(con, tabla, dato, id);
            }

            default -> {
                System.out.println("opcion no valida");
                break;
            }
        }
    }

    public static void eliminarBDD() {
        System.out.println("Elija la tabla donde quiere eliminar informacion");
        System.out.println("Indique la tabla");
        System.out.println("1. Departamento");
        System.out.println("2. Empleado");
        System.out.println("3. Cliente");
        System.out.println("4. Proyecto");

        int id = 0;
        opcion = sc.nextInt();
        String tabla = "";
        
        if(opcion == 1)  tabla = "Departamento";
        if(opcion == 2)  tabla = "Empleado";
        if(opcion == 3)  tabla = "Cliente";
        if(opcion == 4)  tabla = "Proyecto";

        switch (opcion) {
            case 1,2,3,4 -> {
                System.out.println("Indique la id a eliminar");
                id = sc.nextInt();
                Delete.delete(con, tabla, id);
            }
            default -> {
                System.out.println("Opcion no permitida");
            }
        }

    }
    public static void editarBDD() {
        System.out.println("Elija la tabla donde quiere editar informacion");
        System.out.println("Indique la tabla");
        System.out.println("1. Departamento");
        System.out.println("2. Empleado");
        System.out.println("3. Cliente");
        System.out.println("4. Proyecto");

        int id = 0;
        opcion = sc.nextInt();
        String tabla = "";
        
        if(opcion == 1)  tabla = "Departamento";
        if(opcion == 2)  tabla = "Empleado";
        if(opcion == 3)  tabla = "Cliente";
        if(opcion == 4)  tabla = "Proyecto";

        switch (opcion) {
            case 1,2,3,4 -> {
                System.out.println("Indique la id a editar");
                id = sc.nextInt();
                System.out.println("Indique la columna");
                String columna = sc.next();
                System.out.println("Indique el nuevo valor");
                String valor = sc.next();
                Edit.edit(con, tabla, id, columna, valor);
            }
            default -> {
                System.out.println("Opcion no permitida");
            }
        }

    }

}
