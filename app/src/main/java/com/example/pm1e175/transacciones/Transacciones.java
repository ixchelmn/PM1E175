package com.example.pm1e175.transacciones;

public class Transacciones {
    //Nombre de la base de datos

    public static final String NameDatabase = "PM1E175";

    //Tablas de la BD
    public static final String tablacontactos = "contactos";

    //Transacciones de la BD
    public static final String CreateTBContactos =
            "CREATE TABLE contactos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT," +
                    "combopais TEXT, numero INTEGER, nota TEXT, imagen TEXT)";

    public static final String DropTableContactos = "DROP TABLE IF EXISTS contactos";

    //Helpers
    public static final String Empty = "";
}
