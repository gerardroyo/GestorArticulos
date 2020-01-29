package com.gestorarticulos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GestorArticulosHelper extends SQLiteOpenHelper{
    // database version
    private static final int database_VERSION = 1;

    // database name
    private static final String database_NAME = "GestorArticulosDataBase";

    public GestorArticulosHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    private String CREATE_ARTICULOS =
            "CREATE TABLE articulos ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "code TEXT," +
                    "description TEXT," +
                    "pvp FLOAT," +
                    "estoc INTEGER)";

    private String VIEW_HISTORIAL =
            "CREATE TABLE historial ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "code TEXT," +
                    "fecha TEXT," +
                    "cantidad INTEGER," +
                    "tipo TEXT," +
                    "articulo_ID INTEGER," +
                    "FOREIGN KEY(articulo_ID) REFERENCES articulos(_id) ON DELETE CASCADE)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICULOS);
        db.execSQL(VIEW_HISTORIAL);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Database schema upgrade code goes here

        /*String buildSQL = "DROP TABLE IF EXISTS  articulos";
        String buildSQL2 = "DROP TABLE IF EXISTS  historial";


        sqLiteDatabase.execSQL(buildSQL);       // drop previous table
        sqLiteDatabase.execSQL(buildSQL2);

        onCreate(sqLiteDatabase); */              // create the table from the beginning*/

       if(oldVersion < 2) {
           sqLiteDatabase.execSQL(VIEW_HISTORIAL);
       }

    }
}
