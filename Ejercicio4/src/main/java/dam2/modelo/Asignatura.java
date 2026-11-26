package dam2.modelo;

import java.util.ArrayList;
import java.util.List;

public class Asignatura {
    private int id;
    private String nombre;
    private Profesor profesor;
    private Curso curso;
    private List<Alumno> alumnos = new ArrayList<>();

    public Asignatura() {
    }

    public Asignatura(int id, String nombre, Profesor profesor, Curso curso) {
        this.id = id;
        this.nombre = nombre;
        this.profesor = profesor;
        this.curso = curso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " - Profesor: " + profesor.getNombre() + " - Curso: " + curso.getNombre()
                + " - Alumnos: " + alumnos.size();
    }
}
