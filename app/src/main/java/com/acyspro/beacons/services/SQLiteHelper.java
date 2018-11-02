package com.acyspro.beacons.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla
    String sqlCreate = "CREATE TABLE IF NOT EXISTS anuncios(" +
            " id TEXT," +
            " type TEXT," +
            " title TEXT," +
            " description TEXT," +
            " image_full_name TEXT," +
            " image_full_url TEXT," +
            " image_pre_name TEXT," +
            " image_pre_url TEXT," +
            " video_url TEXT," +
            " link_url TEXT," +
            " created_at TEXT," +
            " atributo1 TEXT," +
            " atributo2 TEXT," +
            " atributo3 TEXT," +
            " atributo4 TEXT," +
            " atributo5 TEXT," +
            " PRIMARY KEY (id))";


    public SQLiteHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente
        //      la opción de eliminar la tabla anterior y crearla de nuevo
        //      vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la
        //      tabla antigua a la nueva, por lo que este método debería
        //      ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS anuncios");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }


}
