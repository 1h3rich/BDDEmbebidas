package dao;

import java.sql.SQLException;
import java.util.List;

public interface IDao<T, V> {

    void crearTablaSiNoExiste() throws SQLException;

    List<T> listarTodos() throws SQLException;

    T buscarPorId(V id) throws SQLException;

    boolean insertar(T objeto) throws SQLException;

    boolean modificar(T objeto) throws SQLException;

    boolean eliminar(V id) throws SQLException;
}
