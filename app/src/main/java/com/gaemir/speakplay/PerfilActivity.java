package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Activity de acceso a los datos de usuario
 * Realiza una consulta a la BD con el usuario aportado (Usuario de la app o vecino consultado)
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */

public class PerfilActivity extends AppCompatActivity {

    String usuario, perfil;

    EditText user, nombre, apellidos, email, edad, sexo, usuarioDiscord, juego;

    /**
     * Acciones a desarrollar al crear la Activity
     *
     * @param savedInstanceState Bundle (recursos Android) de la app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //atrapamos los valores obtenidos del activity anterior
        usuario = getIntent().getExtras().getString("usuario");
        perfil = getIntent().getExtras().getString("perfil");

        //Elementos del layout
        user = (EditText) findViewById(R.id.usuario);
        nombre = (EditText) findViewById(R.id.nombre);
        apellidos = (EditText) findViewById(R.id.apellidos);
        email = (EditText) findViewById(R.id.email);
        edad = (EditText) findViewById(R.id.edad);
        sexo = (EditText) findViewById(R.id.sexo);
        usuarioDiscord = (EditText) findViewById(R.id.usuarioDiscord);
        juego = (EditText) findViewById(R.id.usuarioJuego);

        //elementos necesarios para comprobar que existe conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //Comprobamos que existe conexion
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                obtenerDatos(this, Peticion.GET_INFO + "?user=" + usuario);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            String mensaje2 = "No dispone de conexión a internet. \nSpeak and Play necesita conexión a internet para funcionar. \nIntentelo de nuevo más tarde";
            Toast.makeText(PerfilActivity.this, mensaje2, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(PerfilActivity.this, MainActivity.class);

            startActivity(intent);
        }

    }

    /**
     * Realiza la peticion a la url aportada y devuelve todos los valores del usuario
     * @param context contexto app
     * @param url direccion del web service desde donde obtenemos los valores de la consulta
     * @throws JSONException debemos atrapar un error en caso de que el JSON obtenido desde el servidor no sea correcto o no obtengamos un JSON
     */
    public void obtenerDatos(Context context, String url) throws JSONException {

        String TAG = getClass().getName();
        //petición singleton mediante la libreria Volley
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // pasamos lo obtenido al procesador
                        procesarDatos(response);


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
    private void procesarDatos(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    //obtenemos el objeto info
                    JSONArray mensaje = response.getJSONArray("info");

                    //Cargamos los valores del usuario y los mostramos en el formulario
                    for (int i = 0; i < mensaje.length(); i++) {
                        try {
                            JSONObject jsonObject = mensaje.getJSONObject(i);


                            if (perfil.equals("personal")) {
                                user.setText("Usuario: " + usuario);
                                email.setText("Email: " + jsonObject.getString("Email"));
                            } else if (perfil.equals("externo")) {
                                user.setVisibility(View.GONE);
                                email.setVisibility(View.GONE);
                            }


                            nombre.setText("Nombre: " + jsonObject.getString("Nombre"));
                            apellidos.setText("Apellidos: " + jsonObject.getString("Apellidos"));

                            edad.setText("Edad: " + jsonObject.getString("Edad"));
                            String sexonumero = jsonObject.getString("Sexo");

                            if (sexonumero.equals("0")) {
                                sexo.setText("Sexo masculino");
                            } else if (sexonumero.equals("1")) {
                                sexo.setText("Sexo femenino");
                            } else if (sexonumero.equals("1")) {
                                sexo.setText("Sexo oculto");
                            }


                            usuarioDiscord.setText("Usuario Discord: " + jsonObject.getString("Nickdiscord"));
                            juego.setText("Juego: " + jsonObject.getString("Juego"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(PerfilActivity.this, mensaje2, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}