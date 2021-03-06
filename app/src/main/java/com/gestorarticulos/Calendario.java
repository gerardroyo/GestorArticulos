package com.gestorarticulos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Calendario extends AppCompatActivity {

    MainActivity mainActivity = new MainActivity();

    public interface FinalizoCuadroDialogo {
        void ResuladoCuadroDialogo(String num, String date, long _id, int operacion, Cursor linia);
    }

    private FinalizoCuadroDialogo interfaz;

    public Calendario(Context contexto, FinalizoCuadroDialogo actividad, long id, int operacion, Cursor linia) {

        interfaz = actividad;

        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        //dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.calendar);

        final int _operacion = operacion;
        final long _id = id;
        final Cursor _linia = linia;

        final EditText num = (EditText) dialogo.findViewById(R.id.edtNum);
        final TextView date = (TextView) dialogo.findViewById(R.id.tvDate);
        final TextView aceptar = (TextView) dialogo.findViewById(R.id.tvAceptar);
        final TextView cancelar = (TextView) dialogo.findViewById(R.id.tvCancelar);
        final ImageView calendarior = (ImageView) dialogo.findViewById(R.id.imgCalendarior);

        TextView articulo = (TextView) dialogo.findViewById(R.id.tvArticulo);
        articulo.setText(_linia.getString(_linia.getColumnIndex(GestorArticulosDatasource.ARTICULOS_CODE)));

        Calendar c = Calendar.getInstance();
        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int mes = c.get(Calendar.MONTH);
        final int ano = c.get(Calendar.YEAR);

        date.setText(dia + "/" + (mes + 1) + "/" + ano);

        num.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date1 = "";
                try {
                    date1 = FormatoFecha.ChangeFormatDate(date.getText().toString(), "dd/MM/yyyy", "yyyy/MM/dd");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                interfaz.ResuladoCuadroDialogo(num.getText().toString(), date1, _id, _operacion, _linia);
                dialogo.dismiss();
                //mainActivity.refreshTasks();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        calendarior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, dia, mes, ano);
                dpd.show();
            }
        });

        dialogo.show();

    }

}
