package com.example.SQL.Create;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class ScriptSQL {

    /** Crea las tablas ejecutando estructura.sql, solo si todavia no existen. */
    public static void crearSiNoExiste(Connection con) throws IOException, SQLException {
        if (existeTabla(con, "PROFESOR")) {
            System.out.println("Las tablas ya existen, no se ejecuta el script");
            return;
        }
        System.out.println("Creando las tablas (estructura.sql)...");
        ejecutar(con, "/estructura.sql");
        System.out.println("Tablas creadas correctamente");
    }

    // Los identificadores sin comillas se guardan en mayusculas
    private static boolean existeTabla(Connection con, String tabla) throws SQLException {
        try (ResultSet rs = con.getMetaData().getTables(null, null, tabla, null)) {
            return rs.next();
        }
    }

    private static void ejecutar(Connection con, String recurso) throws IOException, SQLException {
        try (InputStream in = ScriptSQL.class.getResourceAsStream(recurso)) {
            if (in == null) {
                throw new IOException("No se encuentra " + recurso);
            }
            String sql = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                    .lines()
                    .map(String::trim)
                    .filter(l -> !l.isEmpty())
                    .filter(l -> !l.startsWith("--")) // comentarios
                    .collect(Collectors.joining("\n"));

            // Todo o nada: si una sentencia falla se deshace lo anterior
            boolean autoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try (Statement st = con.createStatement()) {
                for (String sentencia : sql.split(";")) {
                    if (!sentencia.isBlank()) {
                        st.execute(sentencia);
                    }
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(autoCommit);
            }
        }
    }
}
