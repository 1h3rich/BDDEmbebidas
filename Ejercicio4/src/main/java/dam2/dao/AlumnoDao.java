package dam2.dao;

import com.db4o.ObjectSet;
import dam2.modelo.Alumno;
import dam2.modelo.Asignatura;

import java.util.List;

public class AlumnoDao extends DaoBase<Alumno> {
    public AlumnoDao() {
        super(Alumno.class);
    }

    @Override
    protected int obtenerId(Alumno alumno) {
        return alumno.getId();
    }

    @Override
    protected void copiarDatos(Alumno origen, Alumno destino) {
        destino.setNombre(origen.getNombre());
        destino.setApellidos(origen.getApellidos());
        destino.setAsignaturas(origen.getAsignaturas());
    }

    @Override
    public boolean eliminar(int id) {
        Alumno alumno = buscarPorId(id);
        if (alumno == null) {
            return false;
        }
        for (Asignatura asignatura : alumno.getAsignaturas()) {
            asignatura.getAlumnos().removeIf(actual -> actual.getId() == id);
            db().set(asignatura);
        }
        return super.eliminar(id);
    }

    public List<Alumno> buscarPorNombreQbe(String nombre) {
        Alumno ejemplo = new Alumno();
        ejemplo.setNombre(nombre);
        ObjectSet resultado = db().get(ejemplo);
        return convertir(resultado);
    }
}
