package com.gestorarticulos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Articulo extends AppCompatActivity {

    private long idTask;
    private GestorArticulosDatasource bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        bd = new GestorArticulosDatasource(this);

        // Botones de aceptar y cancelar
        // Boton ok
        Button  btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                aceptarCambios();
            }
        });

        // Boton eliminar
        Button  btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });

        // Boton cancelar
        Button  btnCancel = (Button) findViewById(R.id.btnCancelar);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelarCambios();
            }
        });

        // Busquem el id que estem modificant
        // si el el id es -1 vol dir que s'està creant
        idTask = this.getIntent().getExtras().getLong("id");

        if (idTask != -1) {
            // Si estem modificant carreguem les dades en pantalla
            TextView tv;
            tv = (TextView) findViewById(R.id.edtCodigo);
            tv.setTextColor(Color.parseColor("#676867"));
            tv.setKeyListener(null);

            tv = (TextView) findViewById(R.id.edtEstoc);
            tv.setTextColor(Color.parseColor("#676867"));
            tv.setKeyListener(null);

            cargarDatos();
        }
        else {
            btnDelete.setVisibility(View.GONE);
            SpannableString s = new SpannableString("Añadir Articulo");
            s.setSpan(new TypefaceSpan("monospace"), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setTitle(s);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargarDatos() {

        // Demanem un cursor que retorna un sol registre amb les dades de la tasca
        Cursor datos = bd.task(idTask);
        datos.moveToFirst();

        // Carreguem les dades en la interfície
        TextView tv;

        tv = (TextView) findViewById(R.id.edtCodigo);
        tv.setText(datos.getString(datos.getColumnIndex(GestorArticulosDatasource.ARTICULOS_CODE)));

        SpannableString s = new SpannableString("Modificando: " + datos.getString(datos.getColumnIndex(GestorArticulosDatasource.ARTICULOS_CODE)));
        s.setSpan(new TypefaceSpan("monospace"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(s);

        //setTitle(datos.getString(datos.getColumnIndex(GestorArticulosDatasource.ARTICULOS_CODE)));

        tv = (TextView) findViewById(R.id.edtDescripcion);
        tv.setText(datos.getString(datos.getColumnIndex(GestorArticulosDatasource.ARTICULOS_DESCRIPCION)));

        tv = (TextView) findViewById(R.id.edtPvP);
        tv.setText(datos.getString(datos.getColumnIndex(GestorArticulosDatasource.ARTICULOS_PVP)));

        tv = (TextView) findViewById(R.id.edtEstoc);
        tv.setText(datos.getString(datos.getColumnIndex(GestorArticulosDatasource.ARTICULOS_ESTOC)));

    }

    private void aceptarCambios() {
        // Validem les dades
        TextView tv;

        // code ha d'estar informat
        tv = (TextView) findViewById(R.id.edtCodigo);
        String code = tv.getText().toString();
        if (code.trim().equals("")) {
            myDialogs.showToast(this,"Todos los campos marcados con '*' son OBLIGATORIOS");
            return;
        }


        tv = (TextView) findViewById(R.id.edtDescripcion);
        String descripcion = tv.getText().toString();
        if (descripcion.trim().equals("")) {
            myDialogs.showToast(this,"Todos los campos marcados con '*' son OBLIGATORIOS");
            return;
        }

        tv = (TextView) findViewById(R.id.edtPvP);
        String pvp = tv.getText().toString();
        float pvpFloat = 0;
        if(pvp.isEmpty()) {
        } else {
            try {
                String pvpC = tv.getText().toString().replaceAll(",", ".");
                pvpFloat = Float.valueOf(pvpC);
            }
            catch(Exception e) {
                myDialogs.showToast(this,"P.V.P tienen que ser todo NUMEROS");
                return;
            }
        }

        tv = (TextView) findViewById(R.id.edtEstoc);
        String estoc = tv.getText().toString();
        if (estoc.trim().equals("")) {
            myDialogs.showToast(this,"Todos los campos marcados con '*' son OBLIGATORIOS");
            return;
        }
        int estocInt;
        try {
            estocInt = Integer.parseInt(estoc);
        }
        catch(Exception e) {
            myDialogs.showToast(this,"Stock tienen que ser todo NUMEROS");
            return;
        }

        if (idTask == -1) {
            tv = (TextView) findViewById(R.id.edtCodigo);
            Cursor c = bd.itemCode(tv.getText().toString());
            if (c.getCount() > 0) {
                myDialogs.showToast(this, "Ja existe un articulo con este Codigo");
                return;
            }

            idTask = bd.taskAdd(code, descripcion, pvpFloat, estocInt);
        }
        else {
            bd.taskUpdate(idTask, code, descripcion, pvpFloat, estocInt);

            // ara indiquem si la tasca esta finalitzada o no
            /*EditText chk = (EditText) findViewById(R.id.edtEstoc);
            int value = Integer.parseInt(chk.getText().toString());
            if (descripcion.trim().equals("")) {
                bd.taskPending(idTask, estocInt);
            }
            else {
                bd.taskCompleted(idTask, estocInt);
            }*/
        }

        Intent mIntent = new Intent();
        mIntent.putExtra("id", idTask);
        setResult(RESULT_OK, mIntent);

        finish();
    }

    private void cancelarCambios() {
        Intent mIntent = new Intent();
        mIntent.putExtra("id", idTask);
        setResult(RESULT_CANCELED, mIntent);

        finish();
    }

    private void deleteTask() {

        // Pedimos confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Desitja eliminar la tasca?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.taskDelete(idTask);

                Intent mIntent = new Intent();
                mIntent.putExtra("id", -1);  // Devolvemos -1 indicant que s'ha eliminat
                setResult(RESULT_OK, mIntent);

                finish();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();

    }
}
