package com.gestorarticulos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Articulo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo);

        //getSupportActionBar().setIcon(R.drawable.ic_spa_black_24dp);
        getSupportActionBar().setLogo(R.drawable.ic_spa_black_24dp);

    }
}
