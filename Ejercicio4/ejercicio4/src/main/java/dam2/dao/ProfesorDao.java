package dam2.dao;

public class ProfesorDao implements IDao {
    @Override
    public boolean insertar(Object obj) {
        
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        // Implementación de la eliminación de un profesor por su ID
        return false;
    }

    @Override
    public boolean modificar(Object obj) {
        // Implementación de la modificación de un profesor en la base de datos
        return false;
    }

    @Override
    public Object buscarPorId(int id) {
        // Implementación de la búsqueda de un profesor por su ID
        return null;
    }

    @Override
    public Object buscarTodos() {
        // Implementación de la búsqueda de todos los profesores en la base de datos
        return null;
    }

}
