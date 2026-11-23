package dao;

import database.ConexionBD;
import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * =====================================================================
 *  DAO de Clientes: ClienteDaoImpl
 * =====================================================================
 *
 * Funciona exactamente igual que VideojuegoDaoImpl (mira sus comentarios si
 * tienes dudas sobre PreparedStatement o try-with-resources).
 * Solo cambian la tabla y las columnas.
 *
 * Esto es justo la gracia del patrón DAO + interfaz genérica:
 * todos los DAO tienen los mismos métodos, aunque cada uno trabaje con su tabla.
 *
 * Para conseguir este DAO se usa: DaoFactory.getClienteDao()
 */
public class ClienteDaoImpl implements IDao<Cliente, Integer> {

    /** Pide al Singleton la conexión compartida. */
    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public void crearTablaSiNoExiste() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS cliente (
                    id        INT AUTO_INCREMENT PRIMARY KEY,
                    nombre    VARCHAR(50)  NOT NULL,
                    apellidos VARCHAR(100) NOT NULL,
                    email     VARCHAR(100) UNIQUE,
                    telefono  VARCHAR(20)
                )
                """;
        try (Statement sentencia = obtenerConexion().createStatement()) {
            sentencia.execute(sql);
        }
    }

    @Override
    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT id, nombre, apellidos, email, telefono FROM cliente ORDER BY id";
        List<Cliente> listaClientes = new ArrayList<>();

        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                listaClientes.add(convertirFilaEnCliente(resultado));
            }
        }
        return listaClientes;
    }

    @Override
    public Cliente buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT id, nombre, apellidos, email, telefono FROM cliente WHERE id = ?";

        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertirFilaEnCliente(resultado);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nombre, apellidos, email, telefono) VALUES (?, ?, ?, ?)";

        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getApellidos());
            sentencia.setString(3, cliente.getEmail());
            sentencia.setString(4, cliente.getTelefono());

            int filasInsertadas = sentencia.executeUpdate();

            // Guardamos en el objeto el id que ha generado la base de datos
            try (ResultSet idsGenerados = sentencia.getGeneratedKeys()) {
                if (idsGenerados.next()) {
                    cliente.setId(idsGenerados.getInt(1));
                }
            }
            return filasInsertadas == 1;
        }
    }

    @Override
    public boolean modificar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombre = ?, apellidos = ?, email = ?, telefono = ? WHERE id = ?";

        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql)) {
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getApellidos());
            sentencia.setString(3, cliente.getEmail());
            sentencia.setString(4, cliente.getTelefono());
            sentencia.setInt(5, cliente.getId());
            return sentencia.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql)) {
            sentencia.setInt(1, id);
            // Si el cliente tiene ventas, la clave foránea impide borrarlo -> SQLException
            return sentencia.executeUpdate() > 0;
        }
    }

    /** Convierte la fila actual del ResultSet en un objeto Cliente. */
    private Cliente convertirFilaEnCliente(ResultSet fila) throws SQLException {
        return new Cliente(
                fila.getInt("id"),
                fila.getString("nombre"),
                fila.getString("apellidos"),
                fila.getString("email"),
                fila.getString("telefono")
        );
    }
}
