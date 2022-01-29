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

/**
 * Clase de navegación entre vecinos
 * Esta clase permite navegar entre los diferentes vecinos obenidos en nuestra búsqueda, permite ademmas, realizar solicitudes de amistad o rechazo de las mismas
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */

public class VecinoMain extends AppCompatActivity {
    String usuario, relacion, nombreVecino, userVecino;
    int seleccion, edad;
    Vecino vecino;

    ImageView imagen, atras, adelante, check, dont;
    TextView nombreEdad, juego;

    /**
     * Acciones a desarrollar al crear la Activity
     *
     * @param savedInstanceState Bundle (recursos Android) de la app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vecino_main);

        //elementos de la vista
        imagen = (ImageView) findViewById(R.id.roundedImageView);
        nombreEdad = (TextView) findViewById(R.id.textNombreEdad);
        juego = (TextView) findViewById(R.id.textJuego);
        atras = (ImageView) findViewById(R.id.atras);
        adelante = (ImageView) findViewById(R.id.adelante);
        check = (ImageView) findViewById(R.id.imageCheck);
        dont = (ImageView) findViewById(R.id.imageDont);
        //atrapamos los valores obtenidos del activity anterior
        relacion = getIntent().getExtras().getString("relacion");
        usuario = getIntent().getExtras().getString("usuario");

        //Diferenciamos entre vecinos obtenidos de la busqueda o vecinos que nos han solicitado amistad

        //Si el vecino de nuestra busqueda
        if (relacion.equals("busqueda")) {
            //atrapamos los valores obtenidos del activity anterior
            vecino = getIntent().getParcelableExtra("vecinos");
            seleccion = getIntent().getExtras().getInt("seleccion");
            insertarVecino();
            userVecino = vecino.getUser(seleccion);

            //boton de navegacion
            this.atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seleccion--;
                    insertarVecino();

                }
            });
            //boton de navegacion
            this.adelante.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seleccion++;
                    insertarVecino();

                }
            });

            //Si el vecino nos ha enviado solicitud de amistad
        } else if (relacion.equals("solicitud")) {
            //atrapamos los valores obtenidos del activity anterior
            edad = getIntent().getExtras().getInt("edad");
            nombreVecino = getIntent().getExtras().getString("nombre");
            userVecino = getIntent().getExtras().getString("userVecino");

            nombreEdad.setText(nombreVecino + ", " + edad);
            juego.setText("¿Desea aceptar su solicitud de amistad?");
            juego.setTextSize(20);
            atras.setVisibility(View.INVISIBLE);
            adelante.setVisibility(View.INVISIBLE);
        }


        //Enviar solicitud de amistad o confirmar una solicitud externa
        this.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    solicitarAmistad(VecinoMain.this, Peticion.SOLICITAR_AMISTAD + "?user=" + usuario + "&vecino=" + userVecino);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //Denegar una solicitud de amistad o bloquear a un usuario del que no nos interesa recibir solicitud de amistad
        this.dont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    denegarAmistad(VecinoMain.this, Peticion.DENEGAR_AMISTAD + "?user=" + usuario + "&vecino=" + userVecino);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    /**
     * Pasa los valores obtenidos a la vista
     */
    private void insertarVecino() {


        nombreEdad.setText(vecino.getNombre(seleccion) + ", " + vecino.getEdad(seleccion));

        Context context = getBaseContext();

        int id = context.getResources().getIdentifier(vecino.getImagen(seleccion), "drawable", context.getPackageName());

        imagen.setImageDrawable(getDrawable(id));

        juego.setText(vecino.getJuego(seleccion));

        if (seleccion == 0) {
            atras.setVisibility(View.INVISIBLE);
        } else {
            atras.setVisibility(View.VISIBLE);
        }

        System.out.println(vecino.getSize());
        if (vecino.getSize() - 1 == seleccion) {
            adelante.setVisibility(View.INVISIBLE);
        } else {
            adelante.setVisibility(View.VISIBLE);
        }

    }


    /**
     * Realiza una peticion de amistad, comprueba la situacion en la BD y actua en consecuencia
     * @param context contexto app
     * @param url direccion del web service desde donde obtenemos los valores de la consulta
     * @throws JSONException debemos atrapar un error en caso de que el JSON obtenido desde el servidor no sea correcto o no obtengamos un JSON
     */
    public void solicitarAmistad(Context context, String url) throws JSONException {

        String TAG = getClass().getName();

        //petición singleton mediante la libreria Volley
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // pasamos lo obtenido al procesador
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
     * Interpreta los resultados de la respuesta y así
     * realizar las operaciones correspondientes
     *
     * @param response Objeto Json con la respuesta
     */
    public void procesarSolicitud(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener objeto "mensaje"

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
     * @param context contexto app
     * @param url direccion del web service desde donde obtenemos los valores de la consulta
     * @throws JSONException debemos atrapar un error en caso de que el JSON obtenido desde el servidor no sea correcto o no obtengamos un JSON
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
     * Interpreta los resultados de la respuesta y así
     * realizar las operaciones correspondientes
     *
     * @param response Objeto Json con la respuesta
     */
    public void procesarEnemistad(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener objeto "mensaje"

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