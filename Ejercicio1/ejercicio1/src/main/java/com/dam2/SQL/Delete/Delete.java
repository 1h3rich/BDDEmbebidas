package com.dam2.SQL.Delete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {

    public static void delete(Connection con, String tabla, int condicion) {

        String sql = "DELETE FROM " + tabla + " WHERE id_departamento =" + condicion; // Sentencia sql

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.println(sql);
            ps.executeUpdate(); // ejecuta la consulta
        } catch (SQLException e) {
            System.out.println(
                    "Error en la BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
