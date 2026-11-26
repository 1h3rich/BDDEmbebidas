package dam2.dao;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import dam2.conexion.ConexionDb4o;

import java.util.ArrayList;
import java.util.List;

public abstract class DaoBase<T> implements IDao<T> {
    private final Class<T> tipo;

    protected DaoBase(Class<T> tipo) {
        this.tipo = tipo;
    }

    protected ObjectContainer db() {
        return ConexionDb4o.getInstancia().getBaseDatos();
    }

    protected abstract int obtenerId(T objeto);

    protected abstract void copiarDatos(T origen, T destino);

    @Override
    public boolean insertar(T objeto) {
        if (buscarPorId(obtenerId(objeto)) != null) {
            return false;
        }
        db().set(objeto);
        db().commit();
        return true;
    }

    @Override
    public boolean eliminar(int id) {
        T objeto = buscarPorId(id);
        if (objeto == null) {
            return false;
        }
        db().delete(objeto);
        db().commit();
        return true;
    }

    @Override
    public boolean modificar(T objeto) {
        T guardado = buscarPorId(obtenerId(objeto));
        if (guardado == null) {
            return false;
        }
        copiarDatos(objeto, guardado);
        db().set(guardado);
        db().commit();
        return true;
    }

    @Override
    public T buscarPorId(int id) {
        Query consulta = db().query();
        consulta.constrain(tipo);
        consulta.descend("id").constrain(id);
        ObjectSet resultado = consulta.execute();
        if (resultado.hasNext()) {
            return tipo.cast(resultado.next());
        }
        return null;
    }

    @Override
    public List<T> buscarTodos() {
        ObjectSet resultado = db().query(tipo);
        List<T> objetos = new ArrayList<>();
        while (resultado.hasNext()) {
            objetos.add(tipo.cast(resultado.next()));
        }
        return objetos;
    }

    protected List<T> convertir(ObjectSet resultado) {
        List<T> objetos = new ArrayList<>();
        while (resultado.hasNext()) {
            objetos.add(tipo.cast(resultado.next()));
        }
        return objetos;
    }
}
