package com.gaemir.speakplay;

import androidx.appcompat.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {


    Animation animaBird, animaCorazon, animaSuperman, animaCofre, animaIronman;
    ImageView bird, corazon, superman, cofre, ironman, start;
    EditText user, pass;


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

        bird.startAnimation(animaBird);
        superman.startAnimation(animaSuperman);
        corazon.startAnimation(animaCorazon);
        cofre.startAnimation(animaCofre);


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
                user = (EditText) findViewById(R.id.editTextTextPersonName);
                pass = (EditText) findViewById(R.id.editTextTextPersonName2);

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

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


        this.corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
            }

        });


    }

    /**
     * Realiza la peticion a la url aportada y devuelve un objeto json
     */
    public void procesarPeticion(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
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
     * Interpreta los resultados de la respuesta
     * realizar las operaciones correspondientes
     *
     * @param response Objeto Json con la respuesta
     */
    public void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO


                    JSONObject object = response.getJSONObject("info");

                    int tipo = object.getInt("Tipo");
                    System.out.println(" tipo" + tipo);

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