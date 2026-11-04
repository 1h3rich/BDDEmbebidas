package com.dam2.SQL.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.dam2.utilsLocal.utils;

public class OnOff {

 public static Connection conectarBDD(){
        try{
            Connection con = DriverManager.getConnection(utils.URL);
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void desconectarBDD(Connection con){
        try {
            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
