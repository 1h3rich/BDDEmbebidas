package com.dam2.SQL.Select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class Querys {

    private static final Set<String> TABLAS = Set.of("departamento", "empleado");
    private static final Set<String> COLUMNAS = Set.of("*", "id_departamento", "nombre", "ubicacion");

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

        if (validacionEntrada(dato, tabla)) {
            System.err.println("Tabla o columna no permitida");
            return;
        }

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

        if (validacionEntrada(dato, tabla)) {
            System.err.println("Tabla o columna no permitida");
            return;
        }

        String sql = "SELECT " + dato + " FROM " + tabla + " WHERE id_departamento = " + id; // Sentencia sql

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
        } catch (SQLException e) {
            System.out.println(
                    "Error en la BB: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static boolean validacionEntrada(String dato, String tabla) {
        if (!TABLAS.contains(tabla) || !COLUMNAS.contains(dato)) {
            System.err.println("Tabla o columna no permitida");
            return false;
        }

        return true;
    }
}
