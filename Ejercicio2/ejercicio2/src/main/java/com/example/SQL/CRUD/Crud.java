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
     * Permite obtener un dato de una tabla.
     * SELECT dato FROM tabla.
     * 
     * @con conexion BDD
     * @tabla tabla de la cual queremos datos
     * @dato dato que queremos obtener de la tabla.
     * 
     */
    public static void Query1(Connection con, String tabla, String dato) {

        String sql = "SELECT " + dato + " FROM " + tabla; // Sentencia sql

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) { // evita que haya
                                                                                                   // inserccion sql
                                                                                                   // maliciosa

            while (rs.next()) {
                System.out.println();
                System.out.print("|");
                System.out.print(rs.getString("id_departamento"));
                System.out.print("| ");
                System.out.print(rs.getString("nombre"));
                System.out.print("| ");
                System.out.println(rs.getString("ubicacion"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(
                    "Error en la BB: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Permite obtener un dato de una tabla.
     * SELECT dato FROM tabla.
     * 
     * @con conexion BDD
     * @tabla tabla de la cual queremos datos
     * @dato dato que queremos obtener de la tabla.
     * 
     */
    public static void Query2(Connection con, String tabla, String dato, int id) {

        String sql = "SELECT " + dato + " FROM " + tabla + " WHERE ID = " + id; // Sentencia sql

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) { // evita que haya
                                                                                                   // inserccion sql
                                                                                                   // maliciosa

            while (rs.next()) {
                System.out.println();
                System.out.print("|");
                System.out.print(rs.getString("id_departamento"));
                System.out.print("| ");
                System.out.print(rs.getString("nombre"));
                System.out.print("| ");
                System.out.println(rs.getString("ubicacion"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(
                    "Error en la BB: " + e.getMessage());
            e.printStackTrace();
        }

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
