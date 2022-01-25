package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class VecinoMain extends AppCompatActivity {
    String usuario, relacion,nombreVecino, userVecino;
    int seleccion, edad;
    Vecino vecino;

    ImageView imagen, atras, adelante, check, dont;
    TextView nombreEdad, juego;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vecino_main);

        imagen = (ImageView) findViewById(R.id.roundedImageView);
        nombreEdad = (TextView) findViewById(R.id.textNombreEdad);
        juego = (TextView) findViewById(R.id.textJuego);
        atras = (ImageView) findViewById(R.id.atras);
        adelante = (ImageView) findViewById(R.id.adelante);
        check = (ImageView) findViewById(R.id.imageCheck);
        dont = (ImageView) findViewById(R.id.imageDont);

        relacion =  getIntent().getExtras().getString("relacion");
        usuario = getIntent().getExtras().getString("usuario");


        if(relacion.equals("busqueda")){

            vecino = getIntent().getParcelableExtra("vecinos");
            seleccion = getIntent().getExtras().getInt("seleccion");
            insertarVecino();
            userVecino = vecino.getUser(seleccion);

            this.atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seleccion--;
                    insertarVecino();

                }
            });

            this.adelante.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seleccion++;
                    insertarVecino();

                }
            });
        }else if(relacion.equals("solicitud")){
            edad = getIntent().getExtras().getInt("edad");
            nombreVecino = getIntent().getExtras().getString("nombre");
            userVecino = getIntent().getExtras().getString("userVecino");

            nombreEdad.setText(usuario+", "+ edad);
            juego.setText("¿Desea aceptar su solicitud de amistad?");
            juego.setTextSize(20);
            atras.setVisibility(View.INVISIBLE);
            adelante.setVisibility(View.INVISIBLE);
        }




        this.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    solicitarAmistad(VecinoMain.this, Peticion.SOLICITAR_AMISTAD+"?user="+usuario+"&vecino="+userVecino);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        this.dont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    denegarAmistad(VecinoMain.this, Peticion.DENEGAR_AMISTAD+"?user="+usuario+"&vecino="+userVecino);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });




    }



    private void insertarVecino() {


        nombreEdad.setText(vecino.getNombre(seleccion) + ", " + vecino.getEdad(seleccion));

        Context context = getBaseContext();

        int id = context.getResources().getIdentifier(vecino.getImagen(seleccion), "drawable", context.getPackageName());

        imagen.setImageDrawable(getDrawable(id));

        juego.setText(vecino.getJuego(seleccion));

        if (seleccion == 0) {
            atras.setVisibility(View.INVISIBLE);
        }else{
            atras.setVisibility(View.VISIBLE);
        }

        System.out.println(vecino.getSize());
        if (vecino.getSize() - 1 == seleccion) {
            adelante.setVisibility(View.INVISIBLE);
        }else{
            adelante.setVisibility(View.VISIBLE);
        }

    }



    /**
     * Realiza una peticion de amistad, comprueba la situacion en la BD y actua en consecuencia
     */
    public void solicitarAmistad(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
                        procesarSolicitud(response);


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
     * Gestionar respuesta desde servidor
     *
     * @param response Objeto Json con la respuesta
     */
    public void procesarSolicitud(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener objeto "meta"

                    String mensaje = response.getString("mensaje");
                    Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                    break;

                case "2": // FALLIDO

                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(this, mensaje2, Toast.LENGTH_LONG).show();
                    break;

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Realiza una peticion de amistad, comprueba la situacion en la BD y actua en consecuencia
     */
    public void denegarAmistad(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
                        procesarEnemistad(response);


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
     * Gestionar respuesta desde servidor
     *
     * @param response Objeto Json con la respuesta
     */
    public void procesarEnemistad(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener objeto "meta"

                    String mensaje = response.getString("mensaje");
                    Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                    break;

                case "2": // FALLIDO

                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(this, mensaje2, Toast.LENGTH_LONG).show();
                    break;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}