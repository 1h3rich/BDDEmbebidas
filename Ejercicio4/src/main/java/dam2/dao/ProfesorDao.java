package dam2.dao;

import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import dam2.modelo.Profesor;

import java.util.List;

public class ProfesorDao extends DaoBase<Profesor> {
    public ProfesorDao() {
        super(Profesor.class);
    }

    @Override
    protected int obtenerId(Profesor profesor) {
        return profesor.getId();
    }

    @Override
    protected void copiarDatos(Profesor origen, Profesor destino) {
        destino.setNombre(origen.getNombre());
        destino.setApellidos(origen.getApellidos());
    }

    @Override
    public boolean eliminar(int id) {
        Query consulta = db().query();
        consulta.constrain(dam2.modelo.Asignatura.class);
        consulta.descend("profesor").descend("id").constrain(id);
        if (consulta.execute().hasNext()) {
            return false;
        }
        return super.eliminar(id);
    }

    public List<Profesor> buscarPorApellidoNq(String apellido) {
        ObjectSet resultado = db().query(new Predicate(Profesor.class) {
            public boolean match(Profesor profesor) {
                return profesor.getApellidos().equalsIgnoreCase(apellido);
            }
        });
        return convertir(resultado);
    }
}
