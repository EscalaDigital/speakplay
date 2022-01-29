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

/**
 * Activity principal de acceso administrador
 * Activity de acceso tras un login admin. Nos permite acceder a las demás funciones admin (estadísticas, mapa, perfiles)
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */

public class principalAdminActivity extends AppCompatActivity {

    Button estadisticas, perfiles, mapa;

    /**
     * Acciones a desarrollar al crear la Activity
     *
     * @param savedInstanceState Bundle (recursos Android) de la app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_admin);

        //Elementos de la vista
        estadisticas = (Button) findViewById(R.id.estadisticasBoton);
        perfiles = (Button) findViewById(R.id.perfilesBoton);
        mapa = (Button) findViewById(R.id.mapaBoton);

        //Al pulsar el boton estadísticas accedemos a la pantalla correspondiente
        estadisticas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(principalAdminActivity.this, estadisticasActivity.class);

                startActivity(intent);
            }

        });


        //Al pulsar el boton perfiles accedemos a la pantalla correspondiente
        perfiles.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(principalAdminActivity.this, perfilesActivity.class);
                startActivity(intent);
            }

        });


        //Al pulsar el boton mapa accedemos a la pantalla correspondiente
        mapa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(principalAdminActivity.this, mapaAdminActivity.class);

                startActivity(intent);
            }

        });
    }

}