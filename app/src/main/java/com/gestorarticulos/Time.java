package com.gestorarticulos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Time extends AppCompatActivity {

    TextView barra1;
    TextView barra2;
    TextView barra3;
    TextView detalles;
    TextView tvtvHumedad;
    TextView tvtvViento;

    String imgTiempo;

    ImageView tiempo;
    TextView grados;
    TextView TMaxMin;
    TextView realFeel;
    TextView tiempoTexto;
    ImageView imgHumedad;
    ImageView imgUV;
    TextView humedad;
    TextView viento;
    EditText city;
    Button buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        SpannableString s = new SpannableString("Tiempo Actual");
        s.setSpan(new TypefaceSpan("monospace"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(s);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ConstraintLayout cl = (ConstraintLayout)findViewById(R.id.cl);
        cl.setBackground(getDrawable(R.drawable.fondo));

        barra1 = (TextView)findViewById(R.id.barra1);
        barra2 = (TextView)findViewById(R.id.barra2);
        barra3 = (TextView)findViewById(R.id.barra3);
        detalles = (TextView)findViewById(R.id.tvDetalles);
        tvtvHumedad = (TextView)findViewById(R.id.tvtvHumedad);
        tvtvViento = (TextView)findViewById(R.id.tvtvViento);
        imgHumedad = (ImageView)findViewById(R.id.imgHumedad);
        imgUV = (ImageView)findViewById(R.id.imgUV);

        tiempo = (ImageView)findViewById(R.id.imgTiempo);
        grados = (TextView) findViewById(R.id.tvGrados);
        TMaxMin = (TextView)findViewById(R.id.tvGradosMaxMin);
        realFeel = (TextView) findViewById(R.id.tvRealFeel);
        tiempoTexto = (TextView) findViewById(R.id.tvTiempo);
        humedad = (TextView) findViewById(R.id.tvHumedad);
        viento = (TextView) findViewById(R.id.tvUV);
        city = (EditText) findViewById(R.id.edtCity);
        buscar = (Button) findViewById(R.id.btnBuscar);

        hide();

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sCity = city.getText().toString();

                cargarJSON(sCity);

            }
        });

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

    public void show() {
        barra1.setVisibility(View.VISIBLE);
        barra2.setVisibility(View.VISIBLE);
        barra3.setVisibility(View.VISIBLE);
        detalles.setVisibility(View.VISIBLE);
        tvtvHumedad.setVisibility(View.VISIBLE);
        tvtvViento.setVisibility(View.VISIBLE);
        imgHumedad.setVisibility(View.VISIBLE);
        imgUV.setVisibility(View.VISIBLE);

        tiempo.setVisibility(View.VISIBLE);
        grados.setVisibility(View.VISIBLE);
        TMaxMin.setVisibility(View.VISIBLE);
        realFeel.setVisibility(View.VISIBLE);
        tiempoTexto.setVisibility(View.VISIBLE);
        humedad.setVisibility(View.VISIBLE);
        viento.setVisibility(View.VISIBLE);
    }

    public void hide() {

        barra1.setVisibility(View.GONE);
        barra2.setVisibility(View.GONE);
        barra3.setVisibility(View.GONE);
        detalles.setVisibility(View.GONE);
        tvtvHumedad.setVisibility(View.GONE);
        tvtvViento.setVisibility(View.GONE);
        imgHumedad.setVisibility(View.GONE);
        imgUV.setVisibility(View.GONE);

        tiempo.setVisibility(View.GONE);
        grados.setVisibility(View.GONE);
        TMaxMin.setVisibility(View.GONE);
        realFeel.setVisibility(View.GONE);
        tiempoTexto.setVisibility(View.GONE);
        humedad.setVisibility(View.GONE);
        viento.setVisibility(View.GONE);
    }

    public void cargarInfo(InfoCity tiempoClass){

        imgTiempo = tiempoClass.getImgTiempo();

        URL url = null;
        try {
            url = new URL("https://openweathermap.org/img/wn/"+ tiempoClass.getImgTiempo() +"@2x.png");
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            tiempo.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        grados.setText(tiempoClass.getTiempo());
        TMaxMin.setText(tiempoClass.getMax() + "/ " + tiempoClass.getMin());
        realFeel.setText("RealFeel " + tiempoClass.getRealFeel());
        tiempoTexto.setText(tiempoClass.getTiempoTexto());
        humedad.setText(tiempoClass.getHumedad() + "%");
        viento.setText(tiempoClass.getViento() + "Km/h");

    }

    public void cargarJSON(String city) {

        // Dialogo
        final ProgressDialog Dialog = new ProgressDialog(this);
        Dialog.setCancelable(false);
        Dialog.setCanceledOnTouchOutside(false);


        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0,10000);

        String Url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=61af29f4e8273eaadbf27e7447f3738d";
        client.get(this,Url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Dialog.setMessage("Descargando datos...");
                Dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                //public void onSuccess(String content) {
                Dialog.setMessage("Procesando datos...");

                JSONObject tiempoAPI = null;
                String str = new String(responseBody);
                InfoCity tiempoClass = null;

                try {

                    tiempoAPI = new JSONObject(str);
                    String tiempoTexto = tiempoAPI.getJSONArray("weather").getJSONObject(0).getString("main");
                    String iconoTiempo = tiempoAPI.getJSONArray("weather").getJSONObject(0).getString("icon");

                    Double tiempoGrados = tiempoAPI.getJSONObject("main").getDouble("temp");
                    String humedad = tiempoAPI.getJSONObject("main").getString("humidity");
                    Double realFeel = tiempoAPI.getJSONObject("main").getDouble("feels_like");
                    Double min = tiempoAPI.getJSONObject("main").getDouble("temp_min");
                    Double max = tiempoAPI.getJSONObject("main").getDouble("temp_max");
                    Double viento = tiempoAPI.getJSONObject("wind").getDouble("speed");

                    tiempoClass = new InfoCity(iconoTiempo, tiempoGrados, min, max, realFeel, tiempoTexto, humedad, viento);
                    Log.e("antes", tiempoClass.toString());

                    cargarInfo(tiempoClass);
                    show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Dialog.hide();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                Dialog.hide();

                Toast.makeText(getApplicationContext(), "Introduce un nombre correcto CRACK", Toast.LENGTH_LONG).show();
            }
        });

    }

}
