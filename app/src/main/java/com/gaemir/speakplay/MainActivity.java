package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {


    Animation animaBird, animaCorazon, animaSuperman, animaCofre, animaIronman;
    ImageView bird, corazon, superman, cofre, ironman, start;
    String Servidor, basedatos, usuario, clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtenemos los datos de la BD

        DatosBD datosDB = new DatosBD(this);

        System.out.println(datosDB.getServidor(0) + ", " + datosDB.getServidor(1) + ", " + datosDB.getServidor(2) + ", " + datosDB.getServidor(3));
        Servidor = datosDB.getServidor(0);
        basedatos = datosDB.getServidor(1);
        usuario = datosDB.getServidor(2);
        clave = datosDB.getServidor(2);

        //elementos de la pantalla
        bird = (ImageView) findViewById(R.id.bird);
        corazon = (ImageView) findViewById(R.id.corazon);
        superman = (ImageView) findViewById(R.id.superman);
        cofre = (ImageView) findViewById(R.id.cofre);
        ironman = (ImageView) findViewById(R.id.ironman);
        start = (ImageView) findViewById(R.id.botonstart);


        ironman.setVisibility(View.INVISIBLE);


        animaBird = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bird);
        animaSuperman = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.superman);
        animaCorazon = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.corazon);
        animaCofre = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cofre);
        animaIronman = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ironman);

        bird.startAnimation(animaBird);
        superman.startAnimation(animaSuperman);
        corazon.startAnimation(animaCorazon);
        cofre.startAnimation(animaCofre);


        Intent intent = new Intent(this, MainUser.class);


        this.cofre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == findViewById(R.id.cofre)) {
                    cofre.clearAnimation();
                    cofre.setImageResource(R.drawable.cofreoabierto);
                    int dimensionInPixel = 75;
                    int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());
                    cofre.getLayoutParams().height = dimensionInDp;
                    cofre.getLayoutParams().width = dimensionInDp;

                    ironman.setVisibility(View.VISIBLE);
                    ironman.startAnimation(animaIronman);


                }
            }
        });

        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }

        });


        this.corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
            }

        });


    }


}