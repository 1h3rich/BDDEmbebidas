package com.dam2.Flujo;
import com.dam2.SQL.Create.createDB;

public class Main {
    public static void main(String[] args) {
       createDB.main(args);
        try{
        Menu.menu();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}