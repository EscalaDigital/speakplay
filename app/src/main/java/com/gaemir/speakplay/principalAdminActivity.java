package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;

public class principalAdminActivity extends AppCompatActivity {

    Button estadisticas, perfiles, mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_admin);

        estadisticas = (Button) findViewById(R.id.estadisticasBoton);
        perfiles = (Button) findViewById(R.id.perfilesBoton);
        mapa = (Button) findViewById(R.id.mapaBoton);


        estadisticas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(principalAdminActivity.this, estadisticasActivity.class);

                startActivity(intent);
            }

        });

       perfiles.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(principalAdminActivity.this, perfilesActivity.class);
                startActivity(intent);
            }

        });

        mapa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(principalAdminActivity.this, mapaAdminActivity.class);

                startActivity(intent);
            }

        });
    }

}