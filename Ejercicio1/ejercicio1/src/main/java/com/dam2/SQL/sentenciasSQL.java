package com.dam2.SQL;

import java.sql.*;
import com.dam2.utilsLocal.utils;
import java.util.*;

public class sentenciasSQL {

    public static Connection conectarBDD(){
        try(Connection con = DriverManager.getConnection(utils.URL)) {
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertarDatos(Connection con, String tabla, String[] columnas, Object... valores) throws Exception{
        
        String campos = String.join(", ", columnas); //Nos permite tener Nombre, Apellido, Edad...
        String paramentros = String.join(", ", Collections.nCopies(valores.length, "?")); // Nos permite crear tantos ? como valores
        String sql = "INSERT INTO " + tabla + " (" + campos + ") VALUES (" + paramentros + ")"; // Sentencia sql

        try (PreparedStatement ps = con.prepareStatement(sql)){ // evita que haya inserccion sql maliciosa
            for (int i = 0; i < valores.length; i++){ //recorre un array tan grande como valores
                ps.setObject( i+1, valores[i]); //sustituye el ? por el valor
            }
            ps.executeUpdate(); //ejecuta la consulta
        }
    }

}
