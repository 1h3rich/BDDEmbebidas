package com.dam2.Main;

import com.dam2.SQL.process;
import com.dam2.SQL.sentenciasSQL;

public class Main {
    public static void main(String[] args) {
       process.main(args);
       sentenciasSQL.conectarBDD();
    }
}