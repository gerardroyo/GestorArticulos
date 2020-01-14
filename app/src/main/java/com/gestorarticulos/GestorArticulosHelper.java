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

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ARTICULOS =
                "CREATE TABLE articulos ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "code TEXT," +
                        "description TEXT," +
                        "pvp FLOAT," +
                        "estoc INTEGER)";

        db.execSQL(CREATE_ARTICULOS);
        /*String CREATE_ARTICULOS =
                "CREATE TABLE articulos ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "code TEXT," +
                        "description TEXT," +
                        "pvp FLOAT," +
                        "estoc INTEGER)";

        db.execSQL(CREATE_ARTICULOS);*/
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {


    }
}
