package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * =====================================================================
 * PATRÓN SINGLETON: ConexionBD
 * =====================================================================
 *
 * ¿QUÉ PROBLEMA RESUELVE?
 * Abrir una conexión con la base de datos es "caro" (tarda y gasta recursos).
 * Si cada DAO abriera su propia conexión cada vez, tendríamos muchas conexiones
 * abiertas a la vez y el programa iría más lento.
 *
 * ¿QUÉ HACE EL SINGLETON?
 * Garantiza que de esta clase SOLO EXISTA UN OBJETO en todo el programa.
 * Ese único objeto guarda UNA única conexión, y todos los DAO la comparten.
 *
 * ¿CÓMO SE CONSIGUE? Con 3 ingredientes:
 * 1) Un atributo "private static" que guarda la única instancia.
 * 2) Un constructor "private", para que NADIE de fuera pueda hacer "new
 * ConexionBD()".
 * 3) Un método "public static getInstancia()" que crea la instancia la primera
 * vez y, las siguientes veces, devuelve siempre la misma.
 *
 * ¿CÓMO SE USA DESDE OTRA CLASE?
 * Connection conexion = ConexionBD.getInstancia().getConexion();
 *
 * NOTA: como la conexión es compartida, los DAO NUNCA deben cerrarla
 * (si la cerrasen, los demás DAO se quedarían sin conexión).
 * Solo la cierra el Main, al salir del programa, con cerrarConexion().
 */
public class ConexionBD {

    // ---------- Datos de conexión ----------
    // H2 en modo embebido: la base de datos se guarda en el fichero
    // ./datos/tienda_videojuegos.mv.db
    // (se crea solo la primera vez). "MODE=MySQL" hace que H2 se comporte como
    // MySQL.
    //
    // Si quisieras usar MySQL de verdad, solo cambiarías estas 3 líneas, por
    // ejemplo:
    // URL = "jdbc:mysql://localhost:3306/tienda_videojuegos"; USUARIO = "root";
    // CONTRASENA = "tu_contraseña";
    // (y añadir el conector de MySQL al pom.xml). El resto del programa NO cambia.
    private static final String URL = "jdbc:h2:./datos/tienda_videojuegos;MODE=MySQL";
    private static final String USUARIO = "sa";
    private static final String CONTRASENA = "";

    // ---------- Ingrediente 1: la única instancia ----------
    // Empieza siendo null: todavía no se ha creado.
    private static ConexionBD instancia;

    // La conexión que van a compartir todos los DAO
    private Connection conexion;

    // ---------- Ingrediente 2: constructor privado ----------
    // Al ser private, solo se puede llamar desde DENTRO de esta clase.
    // Fuera de aquí, "new ConexionBD()" da error de compilación.
    private ConexionBD() throws SQLException {
        conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    // ---------- Ingrediente 3: método de acceso ----------
    /**
     * Devuelve la única instancia de ConexionBD.
     * - La primera vez que se llama: instancia es null -> la crea.
     * - Las siguientes veces: ya existe -> devuelve la misma.
     *
     * "synchronized" evita que, si dos hilos llamasen a la vez, se creasen dos
     * instancias.
     * (En este programa solo hay un hilo, pero es una buena costumbre.)
     */
    public static synchronized ConexionBD getInstancia() throws SQLException {
        // Si nunca se ha creado, o si la conexión se cerró por algún motivo, la
        // (re)creamos
        if (instancia == null || instancia.conexion.isClosed()) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    /**
     * Devuelve la conexión compartida para que los DAO puedan lanzar sus consultas.
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Cierra la conexión. Solo se debe llamar UNA vez, al terminar el programa.
     * Es static para poder llamarlo sin crear la instancia si nunca llegó a
     * crearse.
     */
    public static synchronized void cerrarConexion() {
        if (instancia != null) {
            try {
                instancia.conexion.close();
                System.out.println("Conexión con la base de datos cerrada.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
            instancia = null; // así, si alguien vuelve a pedirla, se creará una nueva
        }
    }
}
