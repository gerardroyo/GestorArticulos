package com.gestorarticulos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.entity.mime.Header;

public class Time extends AppCompatActivity {

    ImageView tiempo = (ImageView)findViewById(R.id.imgTiempo);
    TextView grados = (TextView) findViewById(R.id.tvGrados);
    TextView TMaxMin = (TextView)findViewById(R.id.tvGradosMaxMin);
    TextView realFeel = (TextView) findViewById(R.id.tvRealFeel);
    TextView tiempoTexto = (TextView) findViewById(R.id.tvTiempo);
    ImageView imgHumedad = (ImageView) findViewById(R.id.imgHumedad);
    ImageView imgUV = (ImageView) findViewById(R.id.imgUV);
    TextView humedad = (TextView) findViewById(R.id.tvHumedad);
    TextView UV = (TextView) findViewById(R.id.tvUV);
    EditText city = (EditText) findViewById(R.id.edtCity);
    Button buscar = (Button) findViewById(R.id.btnBuscar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
    }

    // Dialogo
    Dialog = new ProgressDialog(this);
        Dialog.setCancelable(false);
        Dialog.setCanceledOnTouchOutside(false);

    private void actualizarPlatos() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0,10000);

        client.addHeader("Authorization",pickingDataSource.API_AUTORITZATION);

        String Url = "api.openweathermap.org/data/2.5/weather?q=" + "{city name" + "&appid=61af29f4e8273eaadbf27e7447f3738d";
        client.get(this,Url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Dialog.setMessage("Descargando platos...");
                Dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //public void onSuccess(String content) {
                Dialog.setMessage("Procesando platos...");

                JSONObject platos = null;
                String str = new String(responseBody);

                try {
                    platos = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Dialog.hide();

                int iPlatos=0;

                try {
                    iPlatos = db.procesarPlatos(platos);
                } catch (JSONException e) {
                    icDialogos.showToastLargo(getApplicationContext(),"Se ha producido un error al procesar las mesas. " + e.getMessage());
                    return;
                }

                if (iPlatos >= 0) {
                    icDialogos.showSnackBarLargo(findViewById(android.R.id.content), "Se han procesado " + String.valueOf(iPlatos) + " platos.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                String str = new String(e.getMessage().toString());
                String valor = "No se ha podido recuperar los datos desde el servidor. " + str;

                Dialog.hide();

                icDialogos.showToast(getApplicationContext(),valor);
            }

        });

    }
}
