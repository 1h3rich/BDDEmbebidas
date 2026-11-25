package dam2.dao;

public class AlumnoDao implements IDao {
    @Override
    public boolean insertar(Object obj) {
        // Implementación de la inserción de un alumno en la base de datos
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        // Implementación de la eliminación de un alumno por su ID
        return false;
    }

    @Override
    public boolean modificar(Object obj) {
        // Implementación de la modificación de un alumno en la base de datos
        return false;
    }

    @Override
    public Object buscarPorId(int id) {
        // Implementación de la búsqueda de un alumno por su ID
        return null;
    }

    @Override
    public Object buscarTodos() {
        // Implementación de la búsqueda de todos los alumnos en la base de datos
        return null;
    }

}
