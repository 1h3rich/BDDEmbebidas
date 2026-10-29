package com.dam2.SQL;

import java.sql.*;
import com.dam2.utilsLocal.utils;

public class sentenciasSQL {

    public static boolean conectarBDD(){
        try(Connection con = DriverManager.getConnection(utils.URL)) {
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void insertarDatos(){
        try(Connection con = DriverManager.getConnection(utils.URL)) {
            Statement stmt = con.createStatement();
            String sql = "INSERT INTO bebidas (nombre, tipo, precio) VALUES ('Coca-Cola', 'Refresco', 1.5), ('Pepsi', 'Refresco', 1.4), ('Fanta', 'Refresco', 1.3), ('Agua', 'Agua', 1.0), ('Cerveza', 'Alcoholica', 2.0)";
            stmt.executeUpdate(sql);
            System.out.println("Datos insertados correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
