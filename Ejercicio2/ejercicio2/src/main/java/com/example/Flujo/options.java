package com.example.Flujo;
import java.util.Scanner;

import com.example.utilsLocal.utils;

public class options {

    public static String decidirConexion(){

        Scanner sc = new Scanner(System.in);
        
        System.out.println("Decide la conexion: ");
        System.out.println("1. APACHE DERBY");
        System.out.println("2. H2");
        System.out.println("3. HSQLDB");
        int opcion = sc.nextInt();
        switch(opcion){
            case 1 -> {
                sc.close();
                return utils.URL_APACHE;
            }
            case 2 ->{
                sc.close();

            }
            case 3 ->{
                sc.close();
             
            }
            default ->{
                System.out.println("opcion no permitida");
            }
        }
        sc.close();
        return "";
    }

}
