package dao;

import database.ConexionBD;
import model.Venta;
import model.VentaCliente;
import model.Videojuego;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class VentaDaoImpl implements IVentaDao {

    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public void crearTablaSiNoExiste() throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS venta (
                    id            INT AUTO_INCREMENT PRIMARY KEY,
                    id_cliente    INT           NOT NULL,
                    id_videojuego INT           NOT NULL,
                    fecha_venta   DATE          NOT NULL,
                    unidades      INT           NOT NULL CHECK (unidades > 0),
                    importe_total DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (id_cliente)    REFERENCES cliente(id),
                    FOREIGN KEY (id_videojuego) REFERENCES videojuego(id)
                )
                """;
        try (Statement sentencia = obtenerConexion().createStatement()) {
            sentencia.execute(sql);
        }
    }

    @Override
    public List<Venta> listarTodos() throws SQLException {
        String sql = "SELECT id, id_cliente, id_videojuego, fecha_venta, unidades, importe_total FROM venta ORDER BY id";
        List<Venta> listaVentas = new ArrayList<>();

        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                listaVentas.add(convertirFilaEnVenta(resultado));
            }
        }
        return listaVentas;
    }

    @Override
    public Venta buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT id, id_cliente, id_videojuego, fecha_venta, unidades, importe_total FROM venta WHERE id = ?";

        try (PreparedStatement sentencia = obtenerConexion().prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return convertirFilaEnVenta(resultado);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insertar(Venta venta) throws SQLException {
        return registrarVentaConStock(venta);
    }

    @Override
    public boolean registrarVentaConStock(Venta venta) throws SQLException {
        Connection conexion = obtenerConexion();
        boolean autoCommit = conexion.getAutoCommit();
        conexion.setAutoCommit(false);

        try {

            Videojuego videojuego = leerVideojuegoDeLaVenta(conexion, venta.getIdVideojuego());
            comprobarStockSuficiente(videojuego, venta.getUnidades());

            venta.setImporteTotal(calcularImporte(videojuego.getPrecio(), venta.getUnidades()));

            String sqlInsertarVenta = "INSERT INTO venta (id_cliente, id_videojuego, fecha_venta, unidades, importe_total) "
                    + "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlInsertarVenta, Statement.RETURN_GENERATED_KEYS)) {
                sentencia.setInt(1, venta.getIdCliente());
                sentencia.setInt(2, venta.getIdVideojuego());
                sentencia.setDate(3, Date.valueOf(venta.getFechaVenta()));
                sentencia.setInt(4, venta.getUnidades());
                sentencia.setDouble(5, venta.getImporteTotal());
                sentencia.executeUpdate();

                try (ResultSet idsGenerados = sentencia.getGeneratedKeys()) {
                    if (idsGenerados.next()) {
                        venta.setId(idsGenerados.getInt(1));
                    }
                }
            }

            sumarAlStock(conexion, venta.getIdVideojuego(), -venta.getUnidades());

            conexion.commit();
            return true;

        } catch (SQLException error) {
            conexion.rollback();
            throw error;
        } finally {
            conexion.setAutoCommit(autoCommit);
        }
    }

    @Override
    public boolean modificar(Venta ventaNueva) throws SQLException {
        Connection conexion = obtenerConexion();
        boolean autoCommit = conexion.getAutoCommit();
        conexion.setAutoCommit(false);

        try {

            Venta ventaAntigua = buscarPorId(ventaNueva.getId());
            if (ventaAntigua == null) {
                conexion.rollback();
                return false;
            }

            sumarAlStock(conexion, ventaAntigua.getIdVideojuego(), ventaAntigua.getUnidades());

            Videojuego videojuego = leerVideojuegoDeLaVenta(conexion, ventaNueva.getIdVideojuego());
            comprobarStockSuficiente(videojuego, ventaNueva.getUnidades());

            ventaNueva.setImporteTotal(calcularImporte(videojuego.getPrecio(), ventaNueva.getUnidades()));
            String sqlModificarVenta = "UPDATE venta SET id_cliente = ?, id_videojuego = ?, fecha_venta = ?, "
                    + "unidades = ?, importe_total = ? WHERE id = ?";
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlModificarVenta)) {
                sentencia.setInt(1, ventaNueva.getIdCliente());
                sentencia.setInt(2, ventaNueva.getIdVideojuego());
                sentencia.setDate(3, Date.valueOf(ventaNueva.getFechaVenta()));
                sentencia.setInt(4, ventaNueva.getUnidades());
                sentencia.setDouble(5, ventaNueva.getImporteTotal());
                sentencia.setInt(6, ventaNueva.getId());
                sentencia.executeUpdate();
            }

            sumarAlStock(conexion, ventaNueva.getIdVideojuego(), -ventaNueva.getUnidades());

            conexion.commit();
            return true;

        } catch (SQLException error) {
            conexion.rollback();
            throw error;
        } finally {
            conexion.setAutoCommit(autoCommit);
        }
    }

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        Connection conexion = obtenerConexion();
        boolean autoCommit = conexion.getAutoCommit();
        conexion.setAutoCommit(false);

        try {
            Venta ventaABorrar = buscarPorId(id);
            if (ventaABorrar == null) {
                conexion.rollback();
                return false;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM venta WHERE id = ?")) {
                sentencia.setInt(1, id);
                sentencia.executeUpdate();
            }

            sumarAlStock(conexion, ventaABorrar.getIdVideojuego(), ventaABorrar.getUnidades());

            conexion.commit();
            return true;

        } catch (SQLException error) {
            conexion.rollback();
            throw error;
        } finally {
            conexion.setAutoCommit(autoCommit);
        }
    }

    @Override
    public List<VentaCliente> consultarVentasCliente(int idCliente) throws SQLException {
        List<VentaCliente> ventas = new ArrayList<>();
        try (CallableStatement llamada = obtenerConexion().prepareCall("{CALL consultar_ventas_cliente(?)}")) {
            llamada.setInt(1, idCliente);
            try (ResultSet resultado = llamada.executeQuery()) {
                while (resultado.next()) {
                    ventas.add(new VentaCliente(
                            resultado.getInt("id_venta"),
                            resultado.getString("nombre_videojuego"),
                            resultado.getDate("fecha_venta").toLocalDate(),
                            resultado.getInt("unidades"),
                            resultado.getBigDecimal("importe")
                    ));
                }
            }
        }
        return ventas;
    }

    @Override
    public BigDecimal consultarTotalGastadoCliente(int idCliente) throws SQLException {
        try (CallableStatement llamada = obtenerConexion().prepareCall("{CALL total_gastado_cliente(?, ?)}")) {
            llamada.setInt(1, idCliente);
            llamada.registerOutParameter(2, Types.DECIMAL);
            llamada.execute();
            BigDecimal total = llamada.getBigDecimal(2);
            return total == null ? BigDecimal.ZERO : total;
        }
    }
    private Videojuego leerVideojuegoDeLaVenta(Connection conexion, int idVideojuego) throws SQLException {
        String sql = "SELECT id, titulo, precio, stock FROM videojuego WHERE id = ? FOR UPDATE";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idVideojuego);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (!resultado.next()) {
                    throw new SQLException("No existe ningún videojuego con id " + idVideojuego + ".");
                }
                Videojuego videojuego = new Videojuego();
                videojuego.setId(resultado.getInt("id"));
                videojuego.setTitulo(resultado.getString("titulo"));
                videojuego.setPrecio(resultado.getDouble("precio"));
                videojuego.setStock(resultado.getInt("stock"));
                return videojuego;
            }
        }
    }

    private void comprobarStockSuficiente(Videojuego videojuego, int unidadesPedidas) throws SQLException {
        if (videojuego.getStock() < unidadesPedidas) {
            throw new SQLException("Stock insuficiente de \"" + videojuego.getTitulo() + "\": quedan "
                    + videojuego.getStock() + " y se piden " + unidadesPedidas + ".");
        }
    }

    private void sumarAlStock(Connection conexion, int idVideojuego, int cantidad) throws SQLException {
        String sql = "UPDATE videojuego SET stock = stock + ? WHERE id = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, cantidad);
            sentencia.setInt(2, idVideojuego);
            sentencia.executeUpdate();
        }
    }

    private double calcularImporte(double precio, int unidades) {
        return Math.round(precio * unidades * 100) / 100.0;
    }

    private Venta convertirFilaEnVenta(ResultSet fila) throws SQLException {
        return new Venta(
                fila.getInt("id"),
                fila.getInt("id_cliente"),
                fila.getInt("id_videojuego"),
                fila.getDate("fecha_venta").toLocalDate(),
                fila.getInt("unidades"),
                fila.getDouble("importe_total")
        );
    }
}
