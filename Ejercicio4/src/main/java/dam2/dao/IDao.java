package dam2.dao;

import java.util.List;

public interface IDao<T> {
    boolean insertar(T objeto);
    boolean eliminar(int id);
    boolean modificar(T objeto);
    T buscarPorId(int id);
    List<T> buscarTodos();
}
