package dao;

import database.ConexionBD;
import model.Venta;
import model.Videojuego;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * =====================================================================
 *  DAO de Ventas: VentaDaoImpl  (con TRANSACCIONES)
 * =====================================================================
 *
 * Las consultas (listar y buscar) son como en los otros DAO.
 * Lo especial está en insertar, modificar y eliminar, porque una venta
 * también cambia el STOCK del videojuego.
 *
 * ---------------------------------------------------------------------
 *  ¿QUÉ ES UNA TRANSACCIÓN?
 * ---------------------------------------------------------------------
 *  Es un grupo de operaciones que deben hacerse TODAS o NINGUNA.
 *
 *  Ejemplo al registrar una venta:
 *     1) INSERT de la venta
 *     2) UPDATE del stock del videojuego (restar las unidades vendidas)
 *
 *  Imagina que el paso 1 funciona pero el 2 falla: tendríamos una venta
 *  registrada y el stock sin descontar -> datos incoherentes.
 *  Con una transacción, si falla cualquier paso, se deshace todo.
 *
 *  Cómo se hace en JDBC (siempre con la MISMA conexión):
 *
 *     conexion.setAutoCommit(false);   // 1. "No guardes nada hasta que yo te diga"
 *     try {
 *         ...operaciones...            // 2. Hacemos todos los pasos
 *         conexion.commit();           // 3. Todo bien -> se guardan TODOS los cambios
 *     } catch (SQLException e) {
 *         conexion.rollback();         // 4. Algo falló -> se deshacen TODOS los cambios
 *         throw e;                     //    y avisamos hacia arriba del error
 *     } finally {
 *         conexion.setAutoCommit(true); // 5. Dejamos la conexión como estaba
 *     }
 *
 *  Por defecto JDBC tiene autoCommit = true: cada sentencia se guarda en cuanto
 *  se ejecuta. Por eso hay que desactivarlo para agrupar varias sentencias.
 *
 *  Como la conexión es la del Singleton, TODAS las operaciones de la transacción
 *  usan la misma conexión (requisito imprescindible para que la transacción funcione).
 *
 * Para conseguir este DAO se usa: DaoFactory.getVentaDao()
 */
public class VentaDaoImpl implements IDao<Venta, Integer> {

    /** Pide al Singleton la conexión compartida. */
    private Connection obtenerConexion() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public void crearTablaSiNoExiste() throws SQLException {
        // Las FOREIGN KEY obligan a que id_cliente e id_videojuego existan en sus tablas.
        // Por eso esta tabla debe crearse DESPUÉS de "cliente" y "videojuego".
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

    /**
     * REGISTRAR UNA VENTA (transacción).
     *
     * Pasos:
     *   1. Comprobar que el videojuego existe.
     *   2. Comprobar que hay stock suficiente.
     *   3. Calcular el importe (precio * unidades).
     *   4. Insertar la venta.
     *   5. Restar las unidades vendidas al stock.
     *   6. commit() si todo va bien  /  rollback() si algo falla.
     */
    @Override
    public boolean insertar(Venta venta) throws SQLException {
        Connection conexion = obtenerConexion();
        conexion.setAutoCommit(false); // Empieza la transacción

        try {
            // Pasos 1 y 2: el videojuego existe y tiene stock suficiente
            Videojuego videojuego = leerVideojuegoDeLaVenta(conexion, venta.getIdVideojuego());
            comprobarStockSuficiente(videojuego, venta.getUnidades());

            // Paso 3: el importe lo calculamos aquí, con el precio real guardado en la BD
            venta.setImporteTotal(calcularImporte(videojuego.getPrecio(), venta.getUnidades()));

            // Paso 4: insertar la venta
            String sqlInsertarVenta = "INSERT INTO venta (id_cliente, id_videojuego, fecha_venta, unidades, importe_total) "
                    + "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlInsertarVenta, Statement.RETURN_GENERATED_KEYS)) {
                sentencia.setInt(1, venta.getIdCliente());
                sentencia.setInt(2, venta.getIdVideojuego());
                sentencia.setDate(3, Date.valueOf(venta.getFechaVenta())); // LocalDate -> java.sql.Date
                sentencia.setInt(4, venta.getUnidades());
                sentencia.setDouble(5, venta.getImporteTotal());
                sentencia.executeUpdate(); // si el cliente no existe, la clave foránea lanza SQLException

                try (ResultSet idsGenerados = sentencia.getGeneratedKeys()) {
                    if (idsGenerados.next()) {
                        venta.setId(idsGenerados.getInt(1));
                    }
                }
            }

            // Paso 5: restar del stock (sumamos una cantidad negativa)
            sumarAlStock(conexion, venta.getIdVideojuego(), -venta.getUnidades());

            // Paso 6a: todo ha ido bien -> guardamos los cambios definitivamente
            conexion.commit();
            return true;

        } catch (SQLException error) {
            // Paso 6b: algo ha fallado -> deshacemos TODO lo hecho desde setAutoCommit(false)
            conexion.rollback();
            throw error; // el Main se encargará de mostrar el mensaje
        } finally {
            // Pase lo que pase, volvemos a dejar la conexión en modo normal
            conexion.setAutoCommit(true);
        }
    }

    /**
     * MODIFICAR UNA VENTA (transacción).
     *
     * Si cambian las unidades o el videojuego, el stock también tiene que cambiar.
     * La forma más sencilla de hacerlo bien es:
     *   1. Devolver al stock las unidades de la venta ANTIGUA (como si se anulase).
     *   2. Comprobar que hay stock para la venta NUEVA.
     *   3. Actualizar la venta con el nuevo importe.
     *   4. Restar del stock las unidades de la venta NUEVA.
     */
    @Override
    public boolean modificar(Venta ventaNueva) throws SQLException {
        Connection conexion = obtenerConexion();
        conexion.setAutoCommit(false);

        try {
            // Leemos cómo era la venta antes del cambio
            Venta ventaAntigua = buscarPorId(ventaNueva.getId());
            if (ventaAntigua == null) {
                conexion.rollback(); // no hemos cambiado nada, pero cerramos la transacción limpiamente
                return false;        // no existe esa venta
            }

            // Paso 1: devolver las unidades antiguas a su videojuego
            sumarAlStock(conexion, ventaAntigua.getIdVideojuego(), ventaAntigua.getUnidades());

            // Paso 2: comprobar el videojuego nuevo (puede ser el mismo) y su stock
            Videojuego videojuego = leerVideojuegoDeLaVenta(conexion, ventaNueva.getIdVideojuego());
            comprobarStockSuficiente(videojuego, ventaNueva.getUnidades());

            // Paso 3: actualizar la venta (recalculando el importe)
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

            // Paso 4: restar las unidades nuevas
            sumarAlStock(conexion, ventaNueva.getIdVideojuego(), -ventaNueva.getUnidades());

            conexion.commit();
            return true;

        } catch (SQLException error) {
            conexion.rollback();
            throw error;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    /**
     * ELIMINAR UNA VENTA (transacción).
     * Al borrar una venta (por ejemplo, una devolución), las unidades vuelven al stock.
     */
    @Override
    public boolean eliminar(Integer id) throws SQLException {
        Connection conexion = obtenerConexion();
        conexion.setAutoCommit(false);

        try {
            Venta ventaABorrar = buscarPorId(id);
            if (ventaABorrar == null) {
                conexion.rollback();
                return false;
            }

            // 1. Borrar la venta
            try (PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM venta WHERE id = ?")) {
                sentencia.setInt(1, id);
                sentencia.executeUpdate();
            }

            // 2. Devolver las unidades al stock del videojuego
            sumarAlStock(conexion, ventaABorrar.getIdVideojuego(), ventaABorrar.getUnidades());

            conexion.commit();
            return true;

        } catch (SQLException error) {
            conexion.rollback();
            throw error;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    // =====================================================================
    //  Métodos auxiliares (privados: solo se usan dentro de esta clase)
    // =====================================================================

    /**
     * Lee el precio y el stock de un videojuego.
     * Si no existe, lanza una SQLException -> eso provoca el rollback() de la transacción.
     *
     * "FOR UPDATE" bloquea esa fila hasta el commit/rollback, para que nadie más
     * pueda cambiar el stock mientras estamos registrando la venta.
     *
     * (Hacemos esta consulta aquí y no con VideojuegoDaoImpl porque así estamos seguros
     * de que va dentro de la misma transacción y usa la misma conexión.)
     */
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

    /** Si no hay unidades suficientes, lanza una SQLException -> provoca el rollback(). */
    private void comprobarStockSuficiente(Videojuego videojuego, int unidadesPedidas) throws SQLException {
        if (videojuego.getStock() < unidadesPedidas) {
            throw new SQLException("Stock insuficiente de \"" + videojuego.getTitulo() + "\": quedan "
                    + videojuego.getStock() + " y se piden " + unidadesPedidas + ".");
        }
    }

    /**
     * Suma "cantidad" al stock de un videojuego.
     * Con cantidad negativa, resta (por ejemplo, -3 quita 3 unidades).
     */
    private void sumarAlStock(Connection conexion, int idVideojuego, int cantidad) throws SQLException {
        String sql = "UPDATE videojuego SET stock = stock + ? WHERE id = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, cantidad);
            sentencia.setInt(2, idVideojuego);
            sentencia.executeUpdate();
        }
    }

    /** precio * unidades, redondeado a 2 decimales (céntimos). */
    private double calcularImporte(double precio, int unidades) {
        return Math.round(precio * unidades * 100) / 100.0;
    }

    /** Convierte la fila actual del ResultSet en un objeto Venta. */
    private Venta convertirFilaEnVenta(ResultSet fila) throws SQLException {
        return new Venta(
                fila.getInt("id"),
                fila.getInt("id_cliente"),
                fila.getInt("id_videojuego"),
                fila.getDate("fecha_venta").toLocalDate(), // java.sql.Date -> LocalDate
                fila.getInt("unidades"),
                fila.getDouble("importe_total")
        );
    }
}
