package dam2.conexion;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import dam2.modelo.Alumno;
import dam2.modelo.Asignatura;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConexionDb4o {
    private static ConexionDb4o instancia;
    private final ObjectContainer baseDatos;

    private ConexionDb4o() {
        Path directorio = Path.of("data");
        try {
            Files.createDirectories(directorio);
        } catch (IOException error) {
            throw new IllegalStateException("No se ha podido crear la carpeta de datos", error);
        }
        Db4o.configure().objectClass(Alumno.class).cascadeOnUpdate(true);
        Db4o.configure().objectClass(Asignatura.class).cascadeOnUpdate(true);
        baseDatos = Db4o.openFile(directorio.resolve("academia.db4o").toString());
    }

    public static synchronized ConexionDb4o getInstancia() {
        if (instancia == null || instancia.baseDatos.ext().isClosed()) {
            instancia = new ConexionDb4o();
        }
        return instancia;
    }

    public ObjectContainer getBaseDatos() {
        return baseDatos;
    }

    public static synchronized void cerrar() {
        if (instancia != null && !instancia.baseDatos.ext().isClosed()) {
            instancia.baseDatos.close();
        }
        instancia = null;
    }
}
