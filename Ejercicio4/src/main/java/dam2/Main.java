package dam2;

import dam2.conexion.ConexionDb4o;
import dam2.dao.AlumnoDao;
import dam2.dao.AsignaturaDao;
import dam2.dao.CursoDao;
import dam2.dao.IDao;
import dam2.dao.ProfesorDao;
import dam2.modelo.Alumno;
import dam2.modelo.Asignatura;
import dam2.modelo.Curso;
import dam2.modelo.Profesor;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner teclado = new Scanner(System.in);
    private static final AlumnoDao alumnoDao = new AlumnoDao();
    private static final ProfesorDao profesorDao = new ProfesorDao();
    private static final AsignaturaDao asignaturaDao = new AsignaturaDao();
    private static final CursoDao cursoDao = new CursoDao();

    public static void main(String[] args) {
        try {
            menuPrincipal();
        } finally {
            ConexionDb4o.cerrar();
        }
    }

    private static void menuPrincipal() {
        int opcion;
        do {
            System.out.println("\n========= ACADEMIA ALIUS =========");
            System.out.println("1. Gestionar alumnos");
            System.out.println("2. Gestionar profesores");
            System.out.println("3. Gestionar asignaturas");
            System.out.println("4. Gestionar cursos");
            System.out.println("5. Consultas Db4o");
            System.out.println("0. Salir");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1 -> menuAlumnos();
                case 2 -> menuProfesores();
                case 3 -> menuAsignaturas();
                case 4 -> menuCursos();
                case 5 -> menuConsultas();
                case 0 -> System.out.println("Hasta pronto");
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private static void menuAlumnos() {
        int opcion;
        do {
            mostrarMenuEntidad("ALUMNOS");
            System.out.println("6. Matricular en asignatura");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1 -> imprimir(alumnoDao.buscarTodos());
                case 2 -> imprimirObjeto(alumnoDao.buscarPorId(leerEntero("ID: ")));
                case 3 -> insertarAlumno();
                case 4 -> modificarAlumno();
                case 5 -> eliminar(alumnoDao, "alumno");
                case 6 -> matricularAlumno();
                case 0 -> {
                }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private static void insertarAlumno() {
        Alumno alumno = new Alumno(leerEntero("ID: "), leerTexto("Nombre: "), leerTexto("Apellidos: "));
        System.out.println(alumnoDao.insertar(alumno) ? "Alumno insertado" : "Ya existe ese ID");
    }

    private static void modificarAlumno() {
        Alumno alumno = alumnoDao.buscarPorId(leerEntero("ID: "));
        if (alumno == null) {
            System.out.println("No existe");
            return;
        }
        alumno.setNombre(leerTexto("Nombre: "));
        alumno.setApellidos(leerTexto("Apellidos: "));
        alumnoDao.modificar(alumno);
        System.out.println("Alumno modificado");
    }

    private static void matricularAlumno() {
        Alumno alumno = alumnoDao.buscarPorId(leerEntero("ID del alumno: "));
        Asignatura asignatura = asignaturaDao.buscarPorId(leerEntero("ID de la asignatura: "));
        if (alumno == null || asignatura == null) {
            System.out.println("No existe el alumno o la asignatura");
            return;
        }
        boolean matriculado = alumno.getAsignaturas().stream().anyMatch(actual -> actual.getId() == asignatura.getId());
        if (matriculado) {
            System.out.println("El alumno ya está matriculado");
            return;
        }
        alumno.getAsignaturas().add(asignatura);
        asignatura.getAlumnos().add(alumno);
        alumnoDao.modificar(alumno);
        asignaturaDao.modificar(asignatura);
        System.out.println("Matrícula realizada");
    }

    private static void menuProfesores() {
        int opcion;
        do {
            mostrarMenuEntidad("PROFESORES");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1 -> imprimir(profesorDao.buscarTodos());
                case 2 -> imprimirObjeto(profesorDao.buscarPorId(leerEntero("ID: ")));
                case 3 -> insertarProfesor();
                case 4 -> modificarProfesor();
                case 5 -> eliminar(profesorDao, "profesor");
                case 0 -> {
                }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private static void insertarProfesor() {
        Profesor profesor = new Profesor(leerEntero("ID: "), leerTexto("Nombre: "), leerTexto("Apellidos: "));
        System.out.println(profesorDao.insertar(profesor) ? "Profesor insertado" : "Ya existe ese ID");
    }

    private static void modificarProfesor() {
        Profesor profesor = profesorDao.buscarPorId(leerEntero("ID: "));
        if (profesor == null) {
            System.out.println("No existe");
            return;
        }
        profesor.setNombre(leerTexto("Nombre: "));
        profesor.setApellidos(leerTexto("Apellidos: "));
        profesorDao.modificar(profesor);
        System.out.println("Profesor modificado");
    }

    private static void menuCursos() {
        int opcion;
        do {
            mostrarMenuEntidad("CURSOS");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1 -> imprimir(cursoDao.buscarTodos());
                case 2 -> imprimirObjeto(cursoDao.buscarPorId(leerEntero("ID: ")));
                case 3 -> insertarCurso();
                case 4 -> modificarCurso();
                case 5 -> eliminar(cursoDao, "curso");
                case 0 -> {
                }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private static void insertarCurso() {
        Curso curso = new Curso(leerEntero("ID: "), leerTexto("Nombre: "));
        System.out.println(cursoDao.insertar(curso) ? "Curso insertado" : "Ya existe ese ID");
    }

    private static void modificarCurso() {
        Curso curso = cursoDao.buscarPorId(leerEntero("ID: "));
        if (curso == null) {
            System.out.println("No existe");
            return;
        }
        curso.setNombre(leerTexto("Nombre: "));
        cursoDao.modificar(curso);
        System.out.println("Curso modificado");
    }

    private static void menuAsignaturas() {
        int opcion;
        do {
            mostrarMenuEntidad("ASIGNATURAS");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1 -> imprimir(asignaturaDao.buscarTodos());
                case 2 -> imprimirObjeto(asignaturaDao.buscarPorId(leerEntero("ID: ")));
                case 3 -> insertarAsignatura();
                case 4 -> modificarAsignatura();
                case 5 -> eliminar(asignaturaDao, "asignatura");
                case 0 -> {
                }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private static void insertarAsignatura() {
        int id = leerEntero("ID: ");
        String nombre = leerTexto("Nombre: ");
        Profesor profesor = profesorDao.buscarPorId(leerEntero("ID del profesor: "));
        Curso curso = cursoDao.buscarPorId(leerEntero("ID del curso: "));
        if (profesor == null || curso == null) {
            System.out.println("No existe el profesor o el curso");
            return;
        }
        Asignatura asignatura = new Asignatura(id, nombre, profesor, curso);
        System.out.println(asignaturaDao.insertar(asignatura) ? "Asignatura insertada" : "Ya existe ese ID");
    }

    private static void modificarAsignatura() {
        Asignatura asignatura = asignaturaDao.buscarPorId(leerEntero("ID: "));
        if (asignatura == null) {
            System.out.println("No existe");
            return;
        }
        Profesor profesor = profesorDao.buscarPorId(leerEntero("ID del profesor: "));
        Curso curso = cursoDao.buscarPorId(leerEntero("ID del curso: "));
        if (profesor == null || curso == null) {
            System.out.println("No existe el profesor o el curso");
            return;
        }
        asignatura.setNombre(leerTexto("Nombre: "));
        asignatura.setProfesor(profesor);
        asignatura.setCurso(curso);
        asignaturaDao.modificar(asignatura);
        System.out.println("Asignatura modificada");
    }

    private static void menuConsultas() {
        System.out.println("\n------ CONSULTAS DB4O ------");
        System.out.println("1. Alumnos por nombre con QBE");
        System.out.println("2. Profesores por apellido con Native Query");
        System.out.println("3. Asignaturas por curso con SODA");
        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> imprimir(alumnoDao.buscarPorNombreQbe(leerTexto("Nombre: ")));
            case 2 -> imprimir(profesorDao.buscarPorApellidoNq(leerTexto("Apellido: ")));
            case 3 -> imprimir(asignaturaDao.buscarPorCursoSoda(leerEntero("ID del curso: ")));
            default -> System.out.println("Opción no válida");
        }
    }

    private static void mostrarMenuEntidad(String entidad) {
        System.out.println("\n------ " + entidad + " ------");
        System.out.println("1. Listar");
        System.out.println("2. Buscar por ID");
        System.out.println("3. Insertar");
        System.out.println("4. Modificar");
        System.out.println("5. Eliminar");
        System.out.println("0. Volver");
    }

    private static <T> void eliminar(IDao<T> dao, String nombre) {
        boolean eliminado = dao.eliminar(leerEntero("ID: "));
        System.out.println(eliminado ? "Se ha eliminado el " + nombre : "No se puede eliminar o no existe");
    }

    private static void imprimir(List<?> objetos) {
        if (objetos.isEmpty()) {
            System.out.println("Sin resultados");
            return;
        }
        objetos.forEach(System.out::println);
    }

    private static void imprimirObjeto(Object objeto) {
        System.out.println(objeto == null ? "No existe" : objeto);
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException error) {
                System.out.println("Escribe un número entero");
            }
        }
    }

    private static String leerTexto(String mensaje) {
        String texto;
        do {
            System.out.print(mensaje);
            texto = teclado.nextLine().trim();
        } while (texto.isEmpty());
        return texto;
    }
}
