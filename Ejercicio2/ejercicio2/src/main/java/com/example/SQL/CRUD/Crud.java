package com.example.SQL.CRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

public class Crud {

    public static void insert(Connection con, String tabla, String[] columnas, Object... valores) {

        String campos = String.join(", ", columnas); // Nos permite tener Nombre, Apellido, Edad...
        String paramentros = String.join(", ", Collections.nCopies(valores.length, "?")); // Nos permite crear tantos ?
                                                                                          // como valores
                                                                                          // Si hay 7 valores, serían 7
                                                                                          // ? --> ?, ?, ?, ?, ?, ?, ?;
        String sql = "INSERT INTO " + tabla + " (" + campos + ") VALUES (" + paramentros + ")"; // Sentencia sql

        try (PreparedStatement ps = con.prepareStatement(sql)) { // evita que haya inserccion sql maliciosa
            for (int i = 0; i < valores.length; i++) { // recorre un array tan grande como valores
                ps.setObject(i + 1, valores[i]); // sustituye el ? por el valor
            }
            ps.executeUpdate(); // ejecuta la consulta
        } catch (SQLException e) {
            System.out.println(
                    "Error en la BB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * SELECT dato FROM tabla. Imprime todas las columnas que devuelva la consulta.
     *
     * @con conexion BDD
     * @tabla tabla de la cual queremos datos
     * @dato columna(s) que queremos obtener (o * para todas)
     */
    public static void Query1(Connection con, String tabla, String dato) {

        String sql = "SELECT " + dato + " FROM " + tabla;

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            imprimir(rs);
        } catch (SQLException e) {
            System.out.println("Error en la BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * SELECT dato FROM tabla WHERE clave1 = ? AND clave2 = ? ...
     */
    public static void Query2(Connection con, String tabla, String dato, String[] columnasClave,
            Object[] valoresClave) {

        String sql = "SELECT " + dato + " FROM " + tabla + " WHERE " + condicion(columnasClave);

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < valoresClave.length; i++) {
                ps.setObject(i + 1, valoresClave[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                imprimir(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error en la BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Imprime cualquier ResultSet: |col1| col2| col3
    private static void imprimir(ResultSet rs) throws SQLException {
        int columnas = rs.getMetaData().getColumnCount();
        boolean hayFilas = false;
        while (rs.next()) {
            hayFilas = true;
            System.out.print("|");
            for (int c = 1; c <= columnas; c++) {
                System.out.print(rs.getString(c));
                System.out.print(c < columnas ? "| " : "");
            }
            System.out.println();
        }
        if (!hayFilas)
            System.out.println("Sin resultados");
    }

    /**
     * UPDATE tabla SET columna = ? WHERE clave1 = ? AND clave2 = ? ...
     *
     * @param columnasClave columnas que forman la clave primaria de la tabla
     * @param valoresClave  valores de esas columnas (mismo orden)
     */
    public static void edit(Connection con, String tabla, String[] columnasClave, Object[] valoresClave,
            String columna, Object valor) {

        String sql = "UPDATE " + tabla + " SET " + columna + " = ? WHERE " + condicion(columnasClave);

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, valor);
            for (int i = 0; i < valoresClave.length; i++) {
                ps.setObject(i + 2, valoresClave[i]);
            }
            int filas = ps.executeUpdate();
            System.out.println(filas + " fila(s) editada(s)");
        } catch (SQLException e) {
            System.out.println("Error en la BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * DELETE FROM tabla WHERE clave1 = ? AND clave2 = ? ...
     */
    public static void delete(Connection con, String tabla, String[] columnasClave, Object[] valoresClave) {

        String sql = "DELETE FROM " + tabla + " WHERE " + condicion(columnasClave);

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < valoresClave.length; i++) {
                ps.setObject(i + 1, valoresClave[i]);
            }
            int filas = ps.executeUpdate();
            System.out.println(filas + " fila(s) eliminada(s)");
        } catch (SQLException e) {
            System.out.println("Error en la BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // clave1 = ? AND clave2 = ? ...
    private static String condicion(String[] columnasClave) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnasClave.length; i++) {
            if (i > 0)
                sb.append(" AND ");
            sb.append(columnasClave[i]).append(" = ?");
        }
        return sb.toString();
    }

}
