package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = valorEntorno("DB_URL", "jdbc:mysql://localhost:3306/tienda_videojuegos");
    private static final String USUARIO = valorEntorno("DB_USER", "root");
    private static final String CONTRASENA = valorEntorno("DB_PASSWORD", "");
    private static ConexionBD instancia;
    private Connection conexion;

    private ConexionBD() throws SQLException {
        conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    public static synchronized ConexionBD getInstancia() throws SQLException {
        if (instancia == null || instancia.conexion.isClosed()) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    public static synchronized void cerrarConexion() {
        if (instancia != null) {
            try {
                instancia.conexion.close();
                System.out.println("Conexión con la base de datos cerrada.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
            instancia = null;
        }
    }

    private static String valorEntorno(String nombre, String valorPorDefecto) {
        String valor = System.getenv(nombre);
        return valor == null || valor.isBlank() ? valorPorDefecto : valor;
    }
}
