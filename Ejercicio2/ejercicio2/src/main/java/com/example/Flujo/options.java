package com.example.Flujo;

import com.example.utilsLocal.utils;

public class options {

    public static String decidirConexion() {

        while (true) {
            System.out.println("Decide la conexion: ");
            System.out.println("1. APACHE DERBY");
            System.out.println("2. H2");
            System.out.println("3. HSQLDB");

            switch (utils.SC.nextInt()) {
                case 1 -> {
                    return utils.URL_APACHE;
                }
                case 2 -> {
                    return utils.URL_H2;
                }
                case 3 -> {
                    return utils.URL_HQLSBD;
                }
                default -> System.out.println("opcion no permitida");
            }
        }
    }

}
