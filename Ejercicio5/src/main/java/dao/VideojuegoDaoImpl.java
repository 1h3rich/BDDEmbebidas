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

public class VideojuegoDaoImpl implements IDao<Videojuego, Integer> {

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

        try (Statement sentencia = obtenerConexion().createStatement()) {
            sentencia.execute(sql);
        }
    }

    @Override
    public List<Videojuego> listarTodos() throws SQLException {
        String sql = "SELECT * FROM videojuego ORDER BY id";
        List<Videojuego> listaVideojuegos = new ArrayList<>();


        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {


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
            sentencia.setInt(1, id);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {
                    return convertirFilaEnVideojuego(resultado);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insertar(Videojuego videojuego) throws SQLException {

        String sql = "INSERT INTO videojuego (titulo, plataforma, genero, precio, stock) VALUES (?, ?, ?, ?, ?)";


        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, videojuego.getTitulo());
            sentencia.setString(2, videojuego.getPlataforma());
            sentencia.setString(3, videojuego.getGenero());
            sentencia.setDouble(4, videojuego.getPrecio());
            sentencia.setInt(5, videojuego.getStock());


            int filasInsertadas = sentencia.executeUpdate();

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
            sentencia.setInt(6, videojuego.getId());

            return sentencia.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        String sql = "DELETE FROM videojuego WHERE id = ?";

        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql)) {
            sentencia.setInt(1, id);


            return sentencia.executeUpdate() > 0;
        }
    }

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
