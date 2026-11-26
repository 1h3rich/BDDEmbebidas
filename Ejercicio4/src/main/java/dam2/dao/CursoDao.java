package dam2.dao;

import com.db4o.query.Query;
import dam2.modelo.Curso;

public class CursoDao extends DaoBase<Curso> {
    public CursoDao() {
        super(Curso.class);
    }

    @Override
    protected int obtenerId(Curso curso) {
        return curso.getId();
    }

    @Override
    protected void copiarDatos(Curso origen, Curso destino) {
        destino.setNombre(origen.getNombre());
    }

    @Override
    public boolean eliminar(int id) {
        Query consulta = db().query();
        consulta.constrain(dam2.modelo.Asignatura.class);
        consulta.descend("curso").descend("id").constrain(id);
        if (consulta.execute().hasNext()) {
            return false;
        }
        return super.eliminar(id);
    }
}
