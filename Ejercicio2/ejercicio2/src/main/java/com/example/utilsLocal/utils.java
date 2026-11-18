package com.example.utilsLocal;

import java.util.Scanner;

public class utils {
    // Las rutas son relativas a la carpeta Ejercicio2 (desde donde se lanza el programa).
    public final static String URL_APACHE = "jdbc:derby:ejercicio2/assets/Actividad2_Derby;create=true"; // Derby
    public final static String URL_H2 = "jdbc:h2:./assets/Actividad2_H2"; // H2
    public final static String URL_HQLSBD = "jdbc:hsqldb:file:assets/Actividad_HSQL"; // HSQLDB

    // Un unico Scanner para todo el programa. No se cierra nunca: cerrarlo cierra System.in.
    public final static Scanner SC = new Scanner(System.in);
}
