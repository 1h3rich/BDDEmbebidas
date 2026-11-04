package com.dam2.SQL.Select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Querys {

    /**
     * Permite obtener un dato de una tabla.
     * SELECT dato FROM tabla. 
     * @con conexion BDD
     * @tabla tabla de la cual queremos datos
     * @dato dato que queremos obtener de la tabla.
     * 
     */
    public static void Query1(Connection con, String tabla, String dato){
       
        String sql = "SELECT " + dato + " FROM " + tabla; // Sentencia sql

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()){ // evita que haya inserccion sql maliciosa
        
            while(rs.next()){
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
                "Error en la BB: " + e.getMessage()
            );
            e.printStackTrace();
        }
                
    }

}
