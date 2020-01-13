package com.gestorarticulos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GestorArticulosHelper extends SQLiteOpenHelper{
    // database version
    private static final int database_VERSION = 2;

    // database name
    private static final String database_NAME = "GestorArticulosDataBase";

    public GestorArticulosHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WAREHOUSEMANAGEMENT =
                "CREATE TABLE articulos ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "code TEXT," +
                        "description TEXT," +
                        "pvp FLOAT," +
                        "estoc INTEGER)";

        db.execSQL(CREATE_WAREHOUSEMANAGEMENT);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Database schema upgrade code goes here

        String buildSQL = "DROP TABLE IF EXISTS articulos";

        sqLiteDatabase.execSQL(buildSQL);       // drop previous table

        onCreate(sqLiteDatabase);               // create the table from the beginning
    }
}
