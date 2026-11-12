package com.example.Flujo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Scanner;
import com.example.SQL.Conexion.*;
import com.example.SQL.Delete.Delete;
import com.example.SQL.Edit.Edit;
import com.example.SQL.Inserts.*;
import com.example.SQL.Select.*;

public class Menu {

    static Connection con = OnOff.conectarBDD(options.decidirConexion()); // Establecemos conexion.

    static int opcion;
    static String tabla;
    static String[] valores;

    static Scanner sc = new Scanner(System.in);

    public static void menu() {

        System.out.println(con.toString());

        System.out.println("Bienvenido a la bdd de la actividad 2");

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
        System.out.println("1. Profesor");
        System.out.println("2. Alumno");
        System.out.println("3. Curso");
        System.out.println("4. Aula");
        System.out.println("5. Asignatura");
        System.out.println("6. Matricula");
        System.out.println("7. Incidencia");
        System.out.println("8. Horario");

        opcion = sc.nextInt();
        String[] columnas;
        Object[] valores;

        String dni, dniAlumno, dniProfesor, temp;
        String nombre, apellidos, direccion, codigoPostal, telefono, descripcion;
        int codigoCurso, codigoAula, codigoAsignatura, idIncidencia;
        int horasSemanales, piso, numeroPupitres;
        int mes, dia, hora;
        BigDecimal calificacion;
        LocalDate fechaNacimiento;

        switch (opcion) {
    case 1:
        System.out.println("--------------PROFESOR--------------");

        System.out.print("DNI: ");
        dni = sc.next();
        System.out.print("Nombre: ");
        nombre = sc.next();
        System.out.print("Apellidos: ");
        apellidos = sc.next();
        System.out.print("Direccion: ");
        direccion = sc.next();
        System.out.print("Nacimiento (YYYY-MM-DD): ");
        temp = sc.next();
        fechaNacimiento = LocalDate.parse(temp);
        System.out.print("Codigo Postal: ");
        codigoPostal = sc.next();
        System.out.print("Telefono: ");
        telefono = sc.next();

        tabla = "PROFESOR";
        columnas = new String[] {
            "dni", "nombre", "apellidos", "direccion",
            "fecha_nacimiento", "codigo_postal", "telefono"
        };
        valores = new Object[] {
            dni, nombre, apellidos, direccion,
            fechaNacimiento, codigoPostal, telefono
        };

        Insert.insertarDatos(con, tabla, columnas, valores);
        break;

    case 2:
        System.out.println("--------------ALUMNO--------------");

        System.out.print("DNI: ");
        dni = sc.next();
        System.out.print("Nombre: ");
        nombre = sc.next();
        System.out.print("Apellidos: ");
        apellidos = sc.next();
        System.out.print("Direccion: ");
        direccion = sc.next();
        System.out.print("Nacimiento (YYYY-MM-DD): ");
        temp = sc.next();
        fechaNacimiento = LocalDate.parse(temp);
        System.out.print("Codigo Postal: ");
        codigoPostal = sc.next();
        System.out.print("Telefono: ");
        telefono = sc.next();

        tabla = "ALUMNO";
        columnas = new String[] {
            "dni", "nombre", "apellidos", "direccion",
            "fecha_nacimiento", "codigo_postal", "telefono"
        };
        valores = new Object[] {
            dni, nombre, apellidos, direccion,
            fechaNacimiento, codigoPostal, telefono
        };

        Insert.insertarDatos(con, tabla, columnas, valores);
        break;

    case 3:
        System.out.println("--------------CURSO--------------");

        System.out.print("Codigo del curso: ");
        codigoCurso = sc.nextInt();
        System.out.print("Nombre del curso: ");
        nombre = sc.next();

        tabla = "CURSO";
        columnas = new String[] { "codigo_curso", "nombre" };
        valores = new Object[] { codigoCurso, nombre };

        Insert.insertarDatos(con, tabla, columnas, valores);
        break;

    case 4:
        System.out.println("--------------AULA--------------");

        System.out.print("Codigo del aula: ");
        codigoAula = sc.nextInt();
        System.out.print("Piso: ");
        piso = sc.nextInt();
        System.out.print("Numero de pupitres: ");
        numeroPupitres = sc.nextInt();

        tabla = "AULA";
        columnas = new String[] {
            "codigo_aula", "piso", "numero_pupitres"
        };
        valores = new Object[] {
            codigoAula, piso, numeroPupitres
        };

        Insert.insertarDatos(con, tabla, columnas, valores);
        break;

    case 5:
        System.out.println("--------------ASIGNATURA--------------");

        System.out.print("Codigo de la asignatura: ");
        codigoAsignatura = sc.nextInt();
        System.out.print("Nombre de la asignatura: ");
        nombre = sc.next();
        System.out.print("Horas semanales: ");
        horasSemanales = sc.nextInt();
        System.out.print("DNI del profesor: ");
        dniProfesor = sc.next();
        System.out.print("Codigo del curso: ");
        codigoCurso = sc.nextInt();

        tabla = "ASIGNATURA";
        columnas = new String[] {
            "codigo_asignatura", "nombre", "horas_semanales",
            "dni_profesor", "codigo_curso"
        };
        valores = new Object[] {
            codigoAsignatura, nombre, horasSemanales,
            dniProfesor, codigoCurso
        };

        Insert.insertarDatos(con, tabla, columnas, valores);
        break;

    case 6:
        System.out.println("--------------MATRICULA--------------");

        System.out.print("DNI del alumno: ");
        dniAlumno = sc.next();
        System.out.print("Codigo de la asignatura: ");
        codigoAsignatura = sc.nextInt();
        System.out.print("Calificacion: ");
        calificacion = new BigDecimal(sc.next());

        tabla = "MATRICULA";
        columnas = new String[] {
            "dni_alumno", "codigo_asignatura", "calificacion"
        };
        valores = new Object[] {
            dniAlumno, codigoAsignatura, calificacion
        };

        Insert.insertarDatos(con, tabla, columnas, valores);
        break;

    case 7:
        System.out.println("--------------INCIDENCIA--------------");

        System.out.print("ID de la incidencia: ");
        idIncidencia = sc.nextInt();
        System.out.print("DNI del alumno: ");
        dniAlumno = sc.next();
        System.out.print("Codigo de la asignatura: ");
        codigoAsignatura = sc.nextInt();
        System.out.print("Descripcion: ");
        descripcion = sc.next();
        System.out.print("Fecha (YYYY-MM-DD): ");
        temp = sc.next();
        fechaNacimiento = LocalDate.parse(temp);

        tabla = "INCIDENCIA";
        columnas = new String[] {
            "id_incidencia", "dni_alumno", "codigo_asignatura",
            "descripcion", "fecha"
        };
        valores = new Object[] {
            idIncidencia, dniAlumno, codigoAsignatura,
            descripcion, fechaNacimiento
        };

        Insert.insertarDatos(con, tabla, columnas, valores);
        break;

    case 8:
        System.out.println("--------------HORARIO--------------");

        System.out.print("Codigo de la asignatura: ");
        codigoAsignatura = sc.nextInt();
        System.out.print("Codigo del aula: ");
        codigoAula = sc.nextInt();
        System.out.print("Mes: ");
        mes = sc.nextInt();
        System.out.print("Dia: ");
        dia = sc.nextInt();
        System.out.print("Hora: ");
        hora = sc.nextInt();

        tabla = "HORARIO";
        columnas = new String[] {
            "codigo_asignatura", "codigo_aula",
            "mes", "dia", "hora"
        };
        valores = new Object[] {
            codigoAsignatura, codigoAula, mes, dia, hora
        };

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
                System.out.println("[PROFESOR]" + //
                                    "[ALUMNO]" + //
                                    "[CURSO]" + //
                                    "[AULA]" + //
                                    "[ASIGNATURA]" + //
                                    "[MATRICULA]" + //
                                    "[INCIDENCIA]" + //
                                    "[HORARIO]");
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
