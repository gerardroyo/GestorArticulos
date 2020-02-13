package com.gestorarticulos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.entity.mime.Header;

public class Time extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        ImageView tiempo = (ImageView)findViewById(R.id.imgTiempo);
        TextView grados = (TextView) findViewById(R.id.tvGrados);
        TextView TMaxMin = (TextView)findViewById(R.id.tvGradosMaxMin);
        TextView realFeel = (TextView) findViewById(R.id.tvRealFeel);
        TextView tiempoTexto = (TextView) findViewById(R.id.tvTiempo);
        ImageView imgHumedad = (ImageView) findViewById(R.id.imgHumedad);
        ImageView imgUV = (ImageView) findViewById(R.id.imgUV);
        TextView humedad = (TextView) findViewById(R.id.tvHumedad);
        TextView UV = (TextView) findViewById(R.id.tvUV);
        final EditText city = (EditText) findViewById(R.id.edtCity);
        Button buscar = (Button) findViewById(R.id.btnBuscar);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sCity = city.getText().toString();

                cargarJSON(sCity);

            }
        });

    }

    public void cargarJSON(String city) {

        // Dialogo
        final ProgressDialog Dialog = new ProgressDialog(this);
        Dialog.setCancelable(false);
        Dialog.setCanceledOnTouchOutside(false);


        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0,10000);

        //client.addHeader("Authorization",pickingDataSource.API_AUTORITZATION);

        String Url = "api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=61af29f4e8273eaadbf27e7447f3738d";
        client.get(this,Url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Dialog.setMessage("Descargando platos...");
                Dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                //public void onSuccess(String content) {
                Dialog.setMessage("Procesando platos...");

                JSONObject tiempoAPI = null;
                InfoCity tiempoClass = null;
                String str = new String(responseBody);

                try {
                    tiempoAPI = new JSONObject(str);
                    String tiempoTexto = tiempoAPI.getJSONObject("weather").getString("main");
                    String iconoTiempo = tiempoAPI.getJSONObject("weather").getString("icon");

                    Double tiempoGrados = tiempoAPI.getJSONObject("main").getDouble("temp");
                    Double humedad = tiempoAPI.getJSONObject("main").getDouble("humidity");
                    Double min = tiempoAPI.getJSONObject("main").getDouble("temp_min");
                    Double max = tiempoAPI.getJSONObject("main").getDouble("temp_max");

                    tiempoClass = new InfoCity();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Dialog.hide();

                /*int iPlatos=0;

                try {
                    iPlatos = db.procesarPlatos(platos);
                } catch (JSONException e) {
                    Snackbar.make(findViewById(android.R.id.content), "Se ha producido un error al procesar las mesas. " + e.getMessage(), Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                if (iPlatos >= 0) {
                    Snackbar.make(findViewById(android.R.id.content), "Se han procesado " + String.valueOf(iPlatos) + " platos.", Snackbar.LENGTH_LONG)
                            .show();
                }*/
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                /*String str = new String(e.getMessage().toString());
                String valor = "No se ha podido recuperar los datos desde el servidor. " + str;

                Dialog.hide();

                Toast.makeText(getApplicationContext(), valor, Toast.LENGTH_LONG).show();*/
                //icDialogos.showToast(getApplicationContext(),valor);
            }
        });

    }

}
