package dam2.dao;

import com.db4o.ObjectSet;
import com.db4o.query.Query;
import dam2.modelo.Alumno;
import dam2.modelo.Asignatura;

import java.util.List;

public class AsignaturaDao extends DaoBase<Asignatura> {
    public AsignaturaDao() {
        super(Asignatura.class);
    }

    @Override
    protected int obtenerId(Asignatura asignatura) {
        return asignatura.getId();
    }

    @Override
    protected void copiarDatos(Asignatura origen, Asignatura destino) {
        destino.setNombre(origen.getNombre());
        destino.setProfesor(origen.getProfesor());
        destino.setCurso(origen.getCurso());
        destino.setAlumnos(origen.getAlumnos());
    }

    @Override
    public boolean eliminar(int id) {
        Asignatura asignatura = buscarPorId(id);
        if (asignatura == null) {
            return false;
        }
        for (Alumno alumno : asignatura.getAlumnos()) {
            alumno.getAsignaturas().removeIf(actual -> actual.getId() == id);
            db().set(alumno);
        }
        return super.eliminar(id);
    }

    public List<Asignatura> buscarPorCursoSoda(int idCurso) {
        Query consulta = db().query();
        consulta.constrain(Asignatura.class);
        consulta.descend("curso").descend("id").constrain(idCurso);
        ObjectSet resultado = consulta.execute();
        return convertir(resultado);
    }
}
