package com.gestorarticulos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GestorArticulosDatasource {
    public static final String table_ARTICULOS = "articulos";
    public static final String TODOLIST_ID = "_id";
    public static final String TODOLIST_CODE = "code";
    public static final String TODOLIST_DESCRIPCION = "description";
    public static final String TODOLIST_PVP = "pvp";
    public static final String TODOLIST_ESTOC = "estoc";

    private GestorArticulosHelper dbHelper;
    private SQLiteDatabase dbW, dbR;

    // CONSTRUCTOR
    public GestorArticulosDatasource(Context ctx) {
        // En el constructor directament obro la comunicació amb la base de dades
        dbHelper = new GestorArticulosHelper(ctx);

        // amés també construeixo dos databases un per llegir i l'altre per alterar
        open();
    }

    // DESTRUCTOR
    protected void finalize () {
        // Cerramos los databases
        dbW.close();
        dbR.close();
    }

    private void open() {
        dbW = dbHelper.getWritableDatabase();
        dbR = dbHelper.getReadableDatabase();
    }

    // ******************
    // Funcions que retornen cursors de todoList
    // ******************
    public Cursor gArticulos() {
        // Retorem totes les tasques
        return dbR.query("articulos", new String[]{TODOLIST_ID,TODOLIST_CODE,TODOLIST_DESCRIPCION,TODOLIST_PVP,TODOLIST_ESTOC},
                null, null,
                null, null, TODOLIST_ID);
    }

    public Cursor gArticulosPending() {
        // Retornem les tasques que el camp DONE = 0
        return dbR.query(table_ARTICULOS, new String[]{TODOLIST_ID,TODOLIST_CODE,TODOLIST_DESCRIPCION,TODOLIST_PVP,TODOLIST_ESTOC},
                TODOLIST_ESTOC + "=?", new String[]{String.valueOf(0)},
                null, null, TODOLIST_ID);
    }

    public Cursor gArticulosCompleted() {
        // Retornem les tasques que el camp DONE = 1
        return dbR.query(table_ARTICULOS, new String[]{TODOLIST_ID,TODOLIST_CODE,TODOLIST_DESCRIPCION,TODOLIST_PVP,TODOLIST_ESTOC},
                TODOLIST_ESTOC + "=?", new String[]{String.valueOf(1)},
                null, null, TODOLIST_ID);
    }

    public Cursor task(long id) {
        // Retorna un cursor només amb el id indicat
        // Retornem les tasques que el camp DONE = 1
        return dbR.query(table_ARTICULOS, new String[]{TODOLIST_ID,TODOLIST_CODE,TODOLIST_DESCRIPCION,TODOLIST_PVP,TODOLIST_ESTOC},
                TODOLIST_ID+ "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }

    // ******************
    // Funciones de manipualación de datos
    // ******************

    public long taskAdd(String code, String description, int pvp, int estoc) {
        // Creem una nova tasca i retornem el id crear per si el necessiten
        ContentValues values = new ContentValues();
        values.put(TODOLIST_CODE, code);
        values.put(TODOLIST_DESCRIPCION, description);
        values.put(TODOLIST_PVP,pvp);
        values.put(TODOLIST_ESTOC,estoc);  // Forcem 0 pq si s'està creant la tasca no pot estar finalitzada

        return dbW.insert(table_ARTICULOS,null,values);
    }

    public void taskUpdate(long id, String code, String description, int pvp, int estoc) {
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues values = new ContentValues();
        values.put(TODOLIST_CODE, code);
        values.put(TODOLIST_DESCRIPCION, description);
        values.put(TODOLIST_PVP,pvp);
        values.put(TODOLIST_ESTOC,estoc);  // Forcem 0 pq si s'està creant la tasca no pot estar finalitzada

        dbW.update(table_ARTICULOS,values, TODOLIST_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void taskDelete(long id) {
        // Eliminem la task amb clau primària "id"
        dbW.delete(table_ARTICULOS,TODOLIST_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void taskPending(long id) {
        // Modifiquem al estat de pendent la task indicada
        ContentValues values = new ContentValues();
        values.put(TODOLIST_ESTOC,0);

        dbW.update(table_ARTICULOS,values, TODOLIST_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void taskCompleted(long id) {
        // Modifiquem al estat de pendent la task indicada
        ContentValues values = new ContentValues();
        values.put(TODOLIST_ESTOC,1);

        dbW.update(table_ARTICULOS,values, TODOLIST_ID + " = ?", new String[] { String.valueOf(id) });
    }
}
