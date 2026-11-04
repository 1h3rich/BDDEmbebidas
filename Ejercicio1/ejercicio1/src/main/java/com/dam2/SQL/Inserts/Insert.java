package com.dam2.SQL.Inserts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;

public class Insert {

    public static void insertarDatos(Connection con, String tabla, String[] columnas, Object... valores){        
       
        String campos = String.join(", ", columnas); //Nos permite tener Nombre, Apellido, Edad...
        String paramentros = String.join(", ", Collections.nCopies(valores.length, "?")); // Nos permite crear tantos ? como valores
                                                                                                    //Si hay 7 valores, serían 7 ? --> ?, ?, ?, ?, ?, ?, ?;
        String sql = "INSERT INTO " + tabla + " (" + campos + ") VALUES (" + paramentros + ")"; // Sentencia sql

            try (PreparedStatement ps = con.prepareStatement(sql)){ // evita que haya inserccion sql maliciosa
                for (int i = 0; i < valores.length; i++){ //recorre un array tan grande como valores
                    ps.setObject( i+1, valores[i]); //sustituye el ? por el valor
                }
                ps.executeUpdate(); //ejecuta la consulta
            } catch (SQLException e) {
                System.out.println(
                    "Error en la BB: " + e.getMessage()
                );
                e.printStackTrace();
            }
        }
        
}
