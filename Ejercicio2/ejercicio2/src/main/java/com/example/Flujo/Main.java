package com.example.Flujo;

import java.sql.Connection;

import com.example.SQL.Conexion.OnOff;
import com.example.SQL.Create.ScriptSQL;

public class Main {
    public static void main(String[] args) {

        String url = options.decidirConexion();

        try (Connection con = OnOff.conectarBDD(url)) {
            if (con == null) {
                System.out.println("No se pudo conectar. Revisa el error de arriba.");
                return;
            }
            ScriptSQL.crearSiNoExiste(con); // crea las tablas si no existen
            Menu.menu(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
