package dao;

import database.ConexionBD;
import model.Videojuego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * =====================================================================
 * DAO de Videojuegos: VideojuegoDaoImpl
 * =====================================================================
 *
 * "Impl" = implementación. Esta clase CUMPLE el contrato IDao<Videojuego,
 * Integer>:
 * escribe el código real (con SQL) de cada método de la interfaz.
 *
 * Fíjate en dos cosas que se repiten en todos los métodos:
 *
 * 1) PreparedStatement con "?":
 * En vez de pegar los valores dentro del texto SQL (peligroso: "inyección
 * SQL"),
 * escribimos "?" y luego rellenamos cada "?" con setString, setInt,
 * setDouble...
 * Los "?" se numeran empezando por 1.
 *
 * 2) try-with-resources: try (PreparedStatement sentencia = ...) { ... }
 * Todo lo que se abre dentro de los paréntesis del try se CIERRA SOLO al
 * terminar,
 * aunque haya un error. Así nunca se quedan recursos JDBC abiertos.
 * OJO: la conexión NO va dentro de esos paréntesis, porque es la del Singleton
 * y la comparten todos los DAO (si la cerrásemos aquí, dejaría de funcionar
 * todo).
 *
 * IMPORTANTE: nadie debe hacer "new VideojuegoDaoImpl()" fuera del paquete
 * factory.
 * Para conseguir este DAO se usa: DaoFactory.getVideojuegoDao()
 */
public class VideojuegoDaoImpl implements IDao<Videojuego, Integer> {

    /** Pide al Singleton la conexión compartida. */
    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public void crearTablaSiNoExiste() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS videojuego (
                    id         INT AUTO_INCREMENT PRIMARY KEY,
                    titulo     VARCHAR(100)  NOT NULL,
                    plataforma VARCHAR(50)   NOT NULL,
                    genero     VARCHAR(50),
                    precio     DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
                    stock      INT           NOT NULL CHECK (stock >= 0)
                )
                """;
        // Statement normal (sin "?") porque esta sentencia no tiene datos variables
        try (Statement sentencia = obtenerConexion().createStatement()) {
            sentencia.execute(sql);
        }
    }

    @Override
    public List<Videojuego> listarTodos() throws SQLException {
        String sql = "SELECT * FROM videojuego ORDER BY id";
        List<Videojuego> listaVideojuegos = new ArrayList<>();

        // Abrimos la sentencia y el resultado; los dos se cierran solos al acabar el
        // try
        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {

            // resultado.next() avanza a la siguiente fila; devuelve false cuando ya no
            // quedan
            while (resultado.next()) {
                listaVideojuegos.add(convertirFilaEnVideojuego(resultado));
            }
        }
        return listaVideojuegos;
    }

    @Override
    public Videojuego buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT ? FROM videojuego WHERE id = ?";

        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql)) {
            sentencia.setInt(1, id); // rellena el primer (y único) "?" con el id

            try (ResultSet resultado = sentencia.executeQuery()) {
                // Como buscamos por clave primaria, habrá como mucho UNA fila: usamos if, no
                // while
                if (resultado.next()) {
                    return convertirFilaEnVideojuego(resultado);
                }
            }
        }
        return null; // no existe ningún videojuego con ese id
    }

    @Override
    public boolean insertar(Videojuego videojuego) throws SQLException {
        // No ponemos el id: lo genera la base de datos (AUTO_INCREMENT)
        String sql = "INSERT INTO videojuego (titulo, plataforma, genero, precio, stock) VALUES (?, ?, ?, ?, ?)";

        // RETURN_GENERATED_KEYS: le pedimos a JDBC que nos devuelva el id que se ha
        // generado
        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, videojuego.getTitulo());
            sentencia.setString(2, videojuego.getPlataforma());
            sentencia.setString(3, videojuego.getGenero());
            sentencia.setDouble(4, videojuego.getPrecio());
            sentencia.setInt(5, videojuego.getStock());

            // executeUpdate() se usa para INSERT, UPDATE y DELETE.
            // Devuelve cuántas filas se han visto afectadas.
            int filasInsertadas = sentencia.executeUpdate();

            // Recogemos el id generado y se lo ponemos al objeto
            try (ResultSet idsGenerados = sentencia.getGeneratedKeys()) {
                if (idsGenerados.next()) {
                    videojuego.setId(idsGenerados.getInt(1));
                }
            }
            return filasInsertadas == 1;
        }
    }

    @Override
    public boolean modificar(Videojuego videojuego) throws SQLException {
        String sql = "UPDATE videojuego SET titulo = ?, plataforma = ?, genero = ?, precio = ?, stock = ? WHERE id = ?";

        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql)) {
            sentencia.setString(1, videojuego.getTitulo());
            sentencia.setString(2, videojuego.getPlataforma());
            sentencia.setString(3, videojuego.getGenero());
            sentencia.setDouble(4, videojuego.getPrecio());
            sentencia.setInt(5, videojuego.getStock());
            sentencia.setInt(6, videojuego.getId()); // el id va en el WHERE, es el último "?"

            // Si no existe ese id, no se modifica ninguna fila -> devuelve 0 -> false
            return sentencia.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        String sql = "DELETE FROM videojuego WHERE id = ?";

        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql)) {
            sentencia.setInt(1, id);
            // Si el videojuego tiene ventas, la base de datos impide borrarlo (clave
            // foránea)
            // y lanza una SQLException, que el Main mostrará al usuario.
            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Método auxiliar: convierte la fila actual del ResultSet en un objeto
     * Videojuego.
     * Así no repetimos este código en listarTodos() y en buscarPorId().
     */
    private Videojuego convertirFilaEnVideojuego(ResultSet fila) throws SQLException {
        return new Videojuego(
                fila.getInt("id"),
                fila.getString("titulo"),
                fila.getString("plataforma"),
                fila.getString("genero"),
                fila.getDouble("precio"),
                fila.getInt("stock"));
    }
}
