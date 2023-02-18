package com.example.pm1e175.configuraciones;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pm1e175.transacciones.Transacciones;

public class SQLiteConexion extends SQLiteOpenHelper {

    //Constructor de clase con Parametros
    public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version){
        super(context, dbname, factory,version);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Lista de las tablas a crear
        sqLiteDatabase.execSQL(Transacciones.CreateTBContactos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(Transacciones.DropTableContactos);

    }
}
