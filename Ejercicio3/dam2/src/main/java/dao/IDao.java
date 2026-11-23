package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * =====================================================================
 *  PATRÓN DAO (Data Access Object): la interfaz genérica IDao<T, V>
 * =====================================================================
 *
 * ¿QUÉ ES UN DAO?
 *   Es una clase cuyo ÚNICO trabajo es hablar con la base de datos para UNA tabla:
 *   guardar, leer, modificar y borrar (CRUD). Todo el SQL vive dentro de los DAO.
 *   El resto del programa (el Main, los menús...) no ve ni una línea de SQL:
 *   solo llama a métodos como dao.insertar(videojuego) o dao.listarTodos().
 *
 * ¿POR QUÉ UNA INTERFAZ?
 *   Una interfaz es un "contrato": dice QUÉ métodos debe tener una clase, pero no
 *   CÓMO se hacen. Así, todos los DAO (de videojuegos, clientes y ventas) tienen
 *   exactamente los mismos métodos, y el Main puede usarlos todos de la misma forma.
 *
 * ¿QUÉ SON <T, V>? (GENÉRICOS)
 *   Son "huecos" para tipos que se rellenan más tarde:
 *     T = el tipo del objeto que maneja el DAO   (Videojuego, Cliente o Venta)
 *     V = el tipo de su clave primaria (id)      (en nuestro caso, Integer)
 *
 *   Ejemplo: "class VideojuegoDaoImpl implements IDao<Videojuego, Integer>"
 *   hace que, dentro de VideojuegoDaoImpl, T sea Videojuego y V sea Integer.
 *   Por eso buscarPorId(V id) se convierte en buscarPorId(Integer id),
 *   y listarTodos() devuelve List<Videojuego>.
 *
 *   Ventaja: escribimos la interfaz UNA vez y vale para cualquier tabla.
 *
 * ¿POR QUÉ "throws SQLException"?
 *   Si algo falla en la base de datos, el DAO no decide qué mostrar por pantalla:
 *   lanza la excepción hacia arriba y el Main decide cómo avisar al usuario.
 */
public interface IDao<T, V> {

    /**
     * Crea la tabla en la base de datos si todavía no existe.
     * (Lo ponemos aquí porque el enunciado exige que TODO el SQL esté dentro de los DAO.)
     */
    void crearTablaSiNoExiste() throws SQLException;

    /** Devuelve todos los registros de la tabla (lista vacía si no hay ninguno). */
    List<T> listarTodos() throws SQLException;

    /** Busca un registro por su id. Devuelve null si no existe. */
    T buscarPorId(V id) throws SQLException;

    /**
     * Inserta un objeto nuevo en la tabla.
     * Al terminar, el objeto recibe el id que le ha asignado la base de datos.
     * @return true si se ha insertado correctamente
     */
    boolean insertar(T objeto) throws SQLException;

    /**
     * Modifica un registro existente (se identifica por el id que lleva el objeto).
     * @return true si se ha modificado; false si no existía ningún registro con ese id
     */
    boolean modificar(T objeto) throws SQLException;

    /**
     * Elimina el registro con ese id.
     * @return true si se ha eliminado; false si no existía
     */
    boolean eliminar(V id) throws SQLException;
}
