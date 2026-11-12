package com.example.Flujo;
import com.example.SQL.Create.createApache;
import com.example.SQL.Create.createH2;
import com.example.SQL.Create.createHSQLDB;

public class Main {
    public static void main(String[] args) {
       createApache.main(args);
       createH2.main(args);
       createHSQLDB.main(args);
       
        try{
        Menu.menu();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}