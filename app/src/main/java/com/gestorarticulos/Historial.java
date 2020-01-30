package com.gestorarticulos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;

public class Historial extends AppCompatActivity implements CalendarioDoble.FinalizoCuadroDialogo2{

    private static int ACTIVITY_TASK_ADD = 1;
    private static int ACTIVITY_TASK_UPDATE = 2;

    public static GestorArticulosDatasource bd;
    private long idActual;
    private int positionActual;
    private adapterTodoIcon scTasks;
    private filterKind filterActual;
    private long id;
    private Cursor dato;
    private Context context = this;
    private Historial _histo;

    //private _histo = (Historial) context;

    private static String[] from = new String[]{ GestorArticulosDatasource.MOVIMIENTOS_CANTIDAD, GestorArticulosDatasource.MOVIMIENTOS_FECHA, GestorArticulosDatasource.MOVIMIENTOS_TIPO};
    private static int[] to = new int[]{R.id.lblCantidad, R.id.lblFecha, R.id.lblTipo};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        //setTitle("Gestor de Articulos");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dato = MainActivity.getCursor();
        id = this.getIntent().getExtras().getLong("id");

        SpannableString s = new SpannableString(dato.getString(dato.getColumnIndex(GestorArticulosDatasource.ARTICULOS_CODE)));
        s.setSpan(new TypefaceSpan("monospace"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        //ActionBar actionBar = getActionBar();
        /*actionBar.*/setTitle(s);

        bd = new GestorArticulosDatasource(this);
        loadTasks();

        //getSupportActionBar().setIcon(R.drawable.ic_spa_black_24dp);
        //getSupportActionBar().setLogo(R.drawable.ic_spa_black_24dp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_historial, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
            case R.id.btnFilter:

                llamarCalendario(this, dato.getInt(dato.getColumnIndexOrThrow(GestorArticulosDatasource.ARTICULOS_ID)), dato);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void llamarCalendario(Context context, long articulo, Cursor dato) {
        new CalendarioDoble(context, Historial.this, articulo, dato);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_TASK_ADD) {
            if (resultCode == RESULT_OK) {
                // Carreguem totes les tasques a lo bestia
                refreshTasks();
            }
        }

        if (requestCode == ACTIVITY_TASK_UPDATE) {
            if (resultCode == RESULT_OK) {
                refreshTasks();
            }
        }

    }*/

    /*private void addTask() {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        idActual = -1;

        Intent i = new Intent(this, Articulo.class );
        i.putExtras(bundle);
        startActivityForResult(i,ACTIVITY_TASK_ADD);
    }*/

    private void loadTasks() {

        // Demanem totes les tasques
        Cursor cursorTasks = bd.gMovimientos(id);

        // Now create a simple cursor adapter and set it to display
        scTasks = new adapterTodoIcon(this, R.layout.row_historial, cursorTasks, from, to, 1);
        //scTasks.oTodoListIcon = this;

        filterActual = filterKind.FILTER_ALL;

        ListView lv = (ListView) findViewById(R.id.lvDades);
        lv.setAdapter(scTasks);

        /*lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {

                        // modifiquem el id
                        updateTask(id);
                    }
                }
        );*/
    }

    public void refreshTasks(long _id, String date1, String date2) {

        Cursor cursorTasks = null;

        // Demanem les tasques depenen del filtre que s'estigui aplicant
        switch (filterActual) {
            case FILTER_ALL:
                cursorTasks = bd.gMovimientos(_id);
                break;
            case FILTER_DATE:
                cursorTasks = bd.gMovimientosDate(_id, date1, date2);
                break;

            default:
                cursorTasks = bd.gArticulos();
        }

        // Notifiquem al adapter que les dades han canviat i que refresqui
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();
    }

    @Override
    public void ResuladoCuadroDialogo2(String date1, String date2, long _id, Cursor linia) throws ParseException {
        // METODE ON RETORNA ELS VALORS DEL DIALOG

        String date1English = FormatoFecha.ChangeFormatDate(date1, "dd/MM/yyyy", "yyyy/MM/dd");
        String date2English = FormatoFecha.ChangeFormatDate(date2, "dd/MM/yyyy", "yyyy/MM/dd");

        /*linia = GestorArticulosDatasource.task(_id);
        linia.moveToFirst();*/

        /*String code = linia.getString(linia.getColumnIndex(GestorArticulosDatasource.ARTICULOS_CODE));
        String description = linia.getString(linia.getColumnIndex(GestorArticulosDatasource.ARTICULOS_DESCRIPCION));
        float pvp = linia.getFloat(linia.getColumnIndex(GestorArticulosDatasource.ARTICULOS_PVP));
        int estoc = linia.getInt(linia.getColumnIndex(GestorArticulosDatasource.ARTICULOS_ESTOC));*/

        /*MainActivity.bd.taskUpdate(_id, code, description, pvp, estocTotal);
        MainActivity.bd.taskAddMov(code, date, numInt, tipo, _id);*/

        filterActual = filterKind.FILTER_DATE;

        refreshTasks(_id, date1English, date2English);
    }

    public void inizRefresh() {
        Cursor cursorTasks = bd.gArticulos();

        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();
    }

    /*private void updateTask(long id) {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);

        idActual = id;


        Intent i = new Intent(this, Articulo.class );
        i.putExtras(bundle);
        startActivityForResult(i,ACTIVITY_TASK_UPDATE);
    }*/

    /*private void filterTot() {
        // Demanem totes les tasques
        Cursor cursorTasks = bd.gArticulos();
        filterActual = filterKind.FILTER_ALL;

        // Notifiquem al adapter que les dades han canviat i que refresqui
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();

        // Ens situem en el primer registre
        ListView lv = (ListView) findViewById(R.id.lvDades);
        lv.setSelection(0);

        Snackbar.make(findViewById(android.R.id.content), "Està visualitzant totes les tasques...", Snackbar.LENGTH_LONG)
                .show();
    }*/

    /*private void filterPendents() {
        // Demanem totes les tasques pendents
        Cursor cursorTasks = bd.gArticulosPending();
        filterActual = filterKind.FILTER_PENDING;

        // Notifiquem al adapter que les dades han canviat i que refresqui
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();

        // Ens situem en el primer registre
        ListView lv = (ListView) findViewById(R.id.lvDades);
        lv.setSelection(0);

        Snackbar.make(findViewById(android.R.id.content), "Esta visualizando los articulos SIN STOCK...", Snackbar.LENGTH_LONG)
                .show();
    }*/

    /*private void filterFinalitzades() {
        // Demanem totes les tasques finalitzades
        Cursor cursorTasks = bd.gArticulosCompleted();
        filterActual = filterKind.FILTER_COMPLETED;

        // Notifiquem al adapter que les dades han canviat i que refresqui
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();

        // Ens situem en el primer registre
        ListView lv = (ListView) findViewById(R.id.lvDades);
        lv.setSelection(0);

        Snackbar.make(findViewById(android.R.id.content), "Esta visualizando los articulos CON STOCK...", Snackbar.LENGTH_LONG)
                .show();
    }*/

    /*private void refreshTasks() {

        Cursor cursorTasks = null;

        // Demanem les tasques depenen del filtre que s'estigui aplicant
        switch (filterActual) {
            case FILTER_ALL:
                cursorTasks = bd.gArticulos();
                break;
            case FILTER_COMPLETED:
                cursorTasks = bd.gArticulosCompleted();
                break;
            case FILTER_PENDING:
                cursorTasks = bd.gArticulosPending();
                break;

            default:
                cursorTasks = bd.gArticulos();
        }

        // Notifiquem al adapter que les dades han canviat i que refresqui
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();
    }*/

    /*public void deleteTask(final int _id) {
        // Pedimos confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Desitja eliminar la tasca?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.taskDelete(_id);
                refreshTasks();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();
    }*/

}

class adapterTodoIcon extends android.widget.SimpleCursorAdapter{

    private static final String colorTaskPending = "#ff6b6b";
    private static final String colorTaskCompleted = "#FFE1E2E1";

    //private GestorArticulosDatasource bd;

    private  Historial oTodoListIcon;
    Context contexto;

    public adapterTodoIcon(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        oTodoListIcon = (Historial) context;
        contexto = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        TextView tv = (TextView) view.findViewById(R.id.lblFecha);
        try {
            tv.setText(FormatoFecha.ChangeFormatDate(tv.getText().toString(), "yyyy/MM/dd", "dd/MM/yyyy"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Agafem l'objecte de la view que es una LINEA DEL CURSOR
        /*Cursor linia = (Cursor) getItem(position);

        int done = linia.getInt(linia.getColumnIndexOrThrow(GestorArticulosDatasource.ARTICULOS_ESTOC));

        // Pintem el fons de la view segons està completada o no
        if (done > 0) {
            view.setBackgroundColor(Color.parseColor(colorTaskCompleted));
        }
        else {
            view.setBackgroundColor(Color.parseColor(colorTaskPending));
        }*/

        // Capturem botons
        /*ImageView btnMensage = (ImageView) view.findViewById(R.id.imgDelete);

        btnMensage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Busco la ROW
                View row = (View) v.getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent().getParent().getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                oTodoListIcon.deleteTask(linia.getInt(linia.getColumnIndexOrThrow(GestorArticulosDatasource.ARTICULOS_ID)));
            }
        });*/

        /*btnMensage = (ImageView) view.findViewById(R.id.imgEntrada);
        btnMensage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Busco la ROW
                View row = (View) v.getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent().getParent().getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                new Calendario(contexto, adapterTodoIcon.this, linia.getInt(linia.getColumnIndexOrThrow(GestorArticulosDatasource.ARTICULOS_ID)), 1, linia);

            }
        });*/

        return view;
    }

    /*@Override
    public void ResuladoCuadroDialogo2(String num, String date, long _id, int operacion, Cursor datos) {
        // METODE ON RETORNA ELS VALORS DEL DIALOG

        String tipo;
        int numInt = 0;

        String code = datos.getString(datos.getColumnIndex(GestorArticulosDatasource.ARTICULOS_CODE));
        String description = datos.getString(datos.getColumnIndex(GestorArticulosDatasource.ARTICULOS_DESCRIPCION));
        float pvp = datos.getFloat(datos.getColumnIndex(GestorArticulosDatasource.ARTICULOS_PVP));
        int estoc = datos.getInt(datos.getColumnIndex(GestorArticulosDatasource.ARTICULOS_ESTOC));

        if(operacion == 1) {
            numInt = Integer.parseInt(num);
            estoc = estoc + numInt;
            tipo = "Entrada";
        } else {
            numInt = Integer.parseInt(num);
            estoc = estoc - numInt;
            tipo = "Salida";
        }

        MainActivity.bd.taskUpdate(_id, code, description, pvp, estoc);
        MainActivity.bd.taskAddMov(code, date, numInt, tipo, _id);

    }*/
}
