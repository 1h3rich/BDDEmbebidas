package com.example.SQL.Edit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Edit {


    public static void edit(Connection con, String tabla, int condicion, String COLUMNA, String VALOR) {

        String sql = "UPDATE " + tabla + " SET " + COLUMNA + " = " + VALOR + " WHERE id_departamento = " + condicion ; // Sentencia sql

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
