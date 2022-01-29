package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONException;
import org.json.JSONObject;


/**
 * Clase de login
 * Esta es la clase principal de la app. Desde ella podremos hacer login, o acceder a la activity de registro
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */

public class MainActivity extends AppCompatActivity {

    //elementos para las animaciones de personajes
    Animation animaBird, animaCorazon, animaSuperman, animaCofre, animaIronman;
    //imagenes de personajes, fondo, boton...
    ImageView bird, corazon, superman, cofre, ironman, start;
    //insertar usuario y password
    EditText user, pass;

    /**
     * Acciones a desarrollar al crear la Activity
     *
     * @param savedInstanceState Bundle (recursos Android) de la app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //elementos de la pantalla
        bird = (ImageView) findViewById(R.id.bird);
        corazon = (ImageView) findViewById(R.id.corazon);
        superman = (ImageView) findViewById(R.id.superman);
        cofre = (ImageView) findViewById(R.id.cofre);
        ironman = (ImageView) findViewById(R.id.ironman);
        start = (ImageView) findViewById(R.id.botonstart);
        ironman.setVisibility(View.INVISIBLE);

        //cargar animaciones (anim) en xml
        animaBird = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bird);
        animaSuperman = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.superman);
        animaCorazon = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.corazon);
        animaCofre = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cofre);
        animaIronman = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ironman);

        //ejecutamos animaciones
        bird.startAnimation(animaBird);
        superman.startAnimation(animaSuperman);
        corazon.startAnimation(animaCorazon);
        cofre.startAnimation(animaCofre);


        //al pulsar la imagen de cofre se ejecuta animacion
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

        //al pulsar el boton start se comprueba que hay internet, si es así se comprueba en la BD que existe usuario y clave y se da acceso
        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = (EditText) findViewById(R.id.editTextTextPersonName);
                pass = (EditText) findViewById(R.id.editTextTextPersonName2);
                //elementos necesarios para comprobar la conexión a internet
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                //comprobamos conexión
                if (networkInfo != null && networkInfo.isConnected()) {
                    try {
                        procesarPeticion(MainActivity.this, Peticion.LOGIN + "?user=" + user.getText() + "&pass=" + pass.getText() + "");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    String mensaje2 = "No dispone de conexión a internet. \nSpeak and Play necesita conexión a internet para funcionar. \nIntentelo de nuevo más tarde";
                    Toast.makeText(MainActivity.this, mensaje2, Toast.LENGTH_LONG).show();
                }


            }

        });

        //al pulsar el corazón redireccionamos a la pantalla de registro
        this.corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
            }

        });


    }

    /**
     * Obtenemos la url y los valores de usuario y clave y se leen los valores desde el serviodr
     * @param context contexto app
     * @param url direccion del web service desde donde obtenemos los valores de la consulta
     * @throws JSONException debemos atrapar un error en caso de que el JSON obtenido desde el servidor no sea correcto o no obtengamos un JSON
     */
    public void procesarPeticion(Context context, String url) throws JSONException {

        String TAG = getClass().getName();
        //petición singleton mediante la libreria Volley
        VolleySingleton.getInstance(context).addToRequestQueue(
                //obtenemos la respuesta
                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // pasamos lo obtenido al un procesador
                        procesarRespuesta(response);


                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e(TAG, "Error Volley: " + error.getMessage());
                            }
                        }

                )
        );


    }

    /**
     * Procesamos la respuesta obtenida desde el servidor y actuamos en consecuencia con los datos
     * @param response Objeto obtenido desde el servidor
     */
    public void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            //analizamos los datos obtenidos y actuamos en consecuencia
            switch (estado) {
                case "1": // EXITO

                    JSONObject object = response.getJSONObject("info");

                    int tipo = object.getInt("Tipo");
                    System.out.println(" tipo" + tipo);


                    //creamos dos accesos diferentes según el tipo de usuario (administrador o generico)
                    if (tipo == 1) {
                        user = (EditText) findViewById(R.id.editTextTextPersonName);
                        Intent intent = new Intent(MainActivity.this, MainUser.class);

                        intent.putExtra("usuario", user.getText().toString());

                        startActivity(intent);
                    } else if (tipo == 0) {

                        user = (EditText) findViewById(R.id.editTextTextPersonName);
                        Intent intent = new Intent(MainActivity.this, principalAdminActivity.class);
                        intent.putExtra("usuario", user.getText().toString());
                        startActivity(intent);
                    }


                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(this, mensaje2, Toast.LENGTH_LONG).show();
                    break;

                case "3": // FALLIDO
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(this, mensaje3, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}