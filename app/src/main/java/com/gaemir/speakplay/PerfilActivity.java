package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

public class PerfilActivity extends AppCompatActivity {

    String usuario;

    EditText user, nombre, apellidos, email, edad, sexo, usuarioDiscord, juego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        usuario = getIntent().getExtras().getString("usuario");

        user = (EditText) findViewById(R.id.usuario);
        nombre = (EditText) findViewById(R.id.nombre);
        apellidos = (EditText) findViewById(R.id.apellidos);
        email = (EditText) findViewById(R.id.email);
        edad = (EditText) findViewById(R.id.edad);
        sexo = (EditText) findViewById(R.id.sexo);
        usuarioDiscord = (EditText) findViewById(R.id.usuarioDiscord);
        juego = (EditText) findViewById(R.id.usuarioJuego);

        try {
            obtenerDatos(this, Peticion.GET_INFO+"?user="+usuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza la peticion a la url aportada y devuelve todos los usuarios dentro de la distancia establecida
     */
    public void obtenerDatos(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
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

                    JSONArray mensaje = response.getJSONArray("info");


                    for (int i = 0; i < mensaje.length(); i++) {
                        try {
                            JSONObject jsonObject = mensaje.getJSONObject(i);


                            user.setText("Usuario: "+ usuario);
                            nombre.setText("Nombre: " + jsonObject.getString("Nombre"));
                            apellidos.setText("Apellidos: " + jsonObject.getString("Apellidos"));
                            email.setText("Email: " + jsonObject.getString("Email"));
                            edad.setText("Edad: " + jsonObject.getString("Edad"));
                            String sexonumero = jsonObject.getString("Sexo");

                            if(sexonumero.equals("0")){
                                sexo.setText("Sexo masculino");
                            }else if(sexonumero.equals("1")){
                                sexo.setText("Sexo femenino");
                            }else if(sexonumero.equals("1")){
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