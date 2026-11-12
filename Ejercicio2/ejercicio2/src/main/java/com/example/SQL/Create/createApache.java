package com.example.SQL.Create;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;

public class createApache {
    public static void main(String[] args) {

        Path apache = Path.of("Programas\\Apache Derby\\bin\\ij.bat"); //Ruta del apache
        Path dbPath = Path.of("Ejercicio2\\ejercicio2\\assets\\"); // Ruta de la base de datos
        Path dbScript = Path.of("ejercicio2\\src\\main\\java\\com\\example\\SQL\\Create\\estructura.sql"); //Ruta de la estructura de la bdd

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "cmd.exe",                    //
                    "/c",                                    //Abre el cmd, ejecuta el siguiente comando, cierrate
                    apache.toAbsolutePath().toString()
            );              

            processBuilder.redirectInput(dbScript.toFile());  // Redirige la entrada del proceso al archivo de script SQL
            processBuilder.redirectErrorStream(true); // Redirige la salida de error al flujo de salida estándar
            Process p = processBuilder.start(); // Inicia el proceso

            System.out.println("Creando la base de datos...");

                if(dbPath.toFile().exists()){
                    System.out.println("La base de datos ya existe.");
                }else{
                    System.out.println("Base de datos creada correctamente.");
                }

            int resultado = p.waitFor();
            if(resultado != 0){
                System.err.println("Error al crear la base de datos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
