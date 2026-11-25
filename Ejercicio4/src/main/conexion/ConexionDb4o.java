public class ConexionDb4o {
    String ruta = "src/main/resources/db4o/BD.db4o";
    ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), ruta);
    public static ConexionDb4o instancia = null;

    private ConexionDb4o() {
        instancia = DriverManager.getConnection("jdbc:db4o:" + ruta);
    }

    public static ConexionDb4o getConexion() {
        if (instancia == null|| instancia.getInstancia().isClosed()) {
            instancia = new ConexionDb4o();
        }
        return instancia;
    }

    public Connection getInstancia() {
        return conexion;
    }

    public static void cerrarConexion() {
        if (instancia != null) {
            try {
                instancia.getInstancia().close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
            instancia = null;
        }
    }
}
