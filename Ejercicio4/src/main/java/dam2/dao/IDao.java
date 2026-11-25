package dam2.dao;

public interface IDao {
    boolean insertar(Object obj);
    boolean eliminar(int id);
    boolean modificar(Object obj);
    Object buscarPorId(int id);
    Object buscarTodos();
}
