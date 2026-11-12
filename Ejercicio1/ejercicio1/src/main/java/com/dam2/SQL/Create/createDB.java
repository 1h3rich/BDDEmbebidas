package com.dam2.SQL.Create;

import java.nio.file.Path;

public class createDB {
    public static void main(String[] args) {

        Path sqlite = Path.of("Programa", "SQLITE", "sqlite3.exe");

        Path dbPath = Path.of(
                "ejercicio1", "assets", "actividad_1.db");

        Path dbScript = Path.of(
                "ejercicio1", "src", "main", "java",
                "com", "dam2", "SQL", "Create", "estructura.sql");

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    sqlite.toString(), // Proceso que permite llamar al sqlite.exe, la ruta de la bdd
                    dbPath.toString(), // llamar al script y si encuentra un fallo detenerse (-bail)
                    "-bail", // El codigo crea un proceso que ejecuta en segundo plano lo necesario para la
                             // bdd
                    dbScript.toString());

            processBuilder.redirectInput(dbScript.toFile()); // Redirige la entrada del proceso al archivo de script SQL
            processBuilder.redirectErrorStream(true); // Redirige la salida de error al flujo de salida estándar
            Process p = processBuilder.start(); // Inicia el proceso

            System.out.println("Creando la base de datos...");

            if (dbPath.toFile().exists()) {
                System.out.println("La base de datos ya existe.");
            } else {
                System.out.println("Base de datos creada correctamente.");
            }

            int resultado = p.waitFor();
            if (resultado != 0) {
                System.err.println("Error al crear la base de datos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
