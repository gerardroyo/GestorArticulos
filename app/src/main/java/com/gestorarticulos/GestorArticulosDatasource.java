package com.gestorarticulos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GestorArticulosDatasource {
    public static final String table_ARTICULOS = "articulos";
    public static final String ARTICULOS_ID = "_id";
    public static final String ARTICULOS_CODE = "code";
    public static final String ARTICULOS_DESCRIPCION = "description";
    public static final String ARTICULOS_PVP = "pvp";
    public static final String ARTICULOS_ESTOC = "estoc";

    public static final String table_MOVIMIENTOS = "historial";
    public static final String MOVIMIENTOS_ID = "_id";
    public static final String MOVIMIENTOS_CODE = "code";
    public static final String MOVIMIENTOS_FECHA = "fecha";
    public static final String MOVIMIENTOS_CANTIDAD = "cantidad";
    public static final String MOVIMIENTOS_TIPO = "tipo";
    public static final String MOVIMIENTOS_ID_ARTICULOS = "articulo_ID";

    private GestorArticulosHelper dbHelper;
    private static SQLiteDatabase dbW, dbR;

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

    // Funcions que retornen cursors de articulos

    public Cursor gArticulos() {
        // Retorem totes les tasques
        return dbR.query(table_ARTICULOS, new String[]{ARTICULOS_ID,ARTICULOS_CODE,ARTICULOS_DESCRIPCION,ARTICULOS_PVP,ARTICULOS_ESTOC},
                null, null,
                null, null, ARTICULOS_ID);
    }

    public Cursor gMovimientos(long id) {
        // Retorem totes les tasques
        return dbR.query(table_MOVIMIENTOS, new String[]{MOVIMIENTOS_ID,MOVIMIENTOS_CODE,MOVIMIENTOS_FECHA,MOVIMIENTOS_CANTIDAD,MOVIMIENTOS_TIPO, MOVIMIENTOS_ID_ARTICULOS},
                MOVIMIENTOS_ID_ARTICULOS + "=?", new String[]{String.valueOf(id)},
                null, null, MOVIMIENTOS_ID);
    }

    public Cursor gArticulosPending() {
        // Retornem les tasques que el camp ESTOC <= 0
        return dbR.query(table_ARTICULOS, new String[]{ARTICULOS_ID,ARTICULOS_CODE,ARTICULOS_DESCRIPCION,ARTICULOS_PVP,ARTICULOS_ESTOC},
                ARTICULOS_ESTOC + "<=" + 0, null,
                null, null, ARTICULOS_ID);
    }

    public Cursor gArticulosCompleted() {
        // Retornem les tasques que el camp ESTOC > 0
        return dbR.query(table_ARTICULOS, new String[]{ARTICULOS_ID,ARTICULOS_CODE,ARTICULOS_DESCRIPCION,ARTICULOS_PVP,ARTICULOS_ESTOC},
                ARTICULOS_ESTOC + ">" + 0, null,
                null, null, ARTICULOS_ID);
    }

    public static Cursor task(long id) {
        // Retorna un cursor només amb el id indicat
        // Retornem les tasques que el camp ESTOC = 1
        return dbR.query(table_ARTICULOS, new String[]{ARTICULOS_ID,ARTICULOS_CODE,ARTICULOS_DESCRIPCION,ARTICULOS_PVP,ARTICULOS_ESTOC},
                ARTICULOS_ID+ "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }

    public Cursor itemCode(String itemCode) {
        return dbR.query(table_ARTICULOS, new String[]{ARTICULOS_ID,ARTICULOS_CODE,ARTICULOS_DESCRIPCION,ARTICULOS_PVP,ARTICULOS_ESTOC},
                ARTICULOS_CODE+ "=?", new String[]{String.valueOf(itemCode)},
                null, null, null);

    }

    // ******************
    // Funciones de manipualación de datos
    // ******************

    public long taskAdd(String code, String description, float pvp, int estoc) {
        // Creem una nova tasca i retornem el id crear per si el necessiten
        ContentValues values = new ContentValues();
        values.put(ARTICULOS_CODE, code);
        values.put(ARTICULOS_DESCRIPCION, description);
        values.put(ARTICULOS_PVP,pvp);
        values.put(ARTICULOS_ESTOC,estoc);

        return dbW.insert(table_ARTICULOS,null,values);
    }

    public long taskAddMov(String code, String fecha, int cantidad, String tipo, long id_articulo) {
        // Creem una nova tasca i retornem el id crear per si el necessiten
        ContentValues values = new ContentValues();
        values.put(MOVIMIENTOS_CODE, code);
        values.put(MOVIMIENTOS_FECHA, fecha);
        values.put(MOVIMIENTOS_CANTIDAD,cantidad);
        values.put(MOVIMIENTOS_TIPO,tipo);
        values.put(MOVIMIENTOS_ID_ARTICULOS,id_articulo);

        return dbW.insert(table_MOVIMIENTOS,null,values);
    }

    public void taskUpdate(long id, String code, String description, float pvp, int estoc) {
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues values = new ContentValues();
        values.put(ARTICULOS_CODE, code);
        values.put(ARTICULOS_DESCRIPCION, description);
        values.put(ARTICULOS_PVP,pvp);
        values.put(ARTICULOS_ESTOC,estoc);

        dbW.update(table_ARTICULOS,values, ARTICULOS_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void taskDelete(long id) {
        // Eliminem la task amb clau primària "id"
        dbW.delete(table_ARTICULOS,ARTICULOS_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void taskPending(long id, int estoc) {
        // Modifiquem al estat de pendent la task indicada
        ContentValues values = new ContentValues();
        values.put(ARTICULOS_ESTOC,estoc);

        dbW.update(table_ARTICULOS,values, ARTICULOS_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void taskCompleted(long id, int estoc) {
        // Modifiquem al estat de pendent la task indicada
        ContentValues values = new ContentValues();
        values.put(ARTICULOS_ESTOC,estoc);

        dbW.update(table_ARTICULOS,values, ARTICULOS_ID + " = ?", new String[] { String.valueOf(id) });
    }
}
