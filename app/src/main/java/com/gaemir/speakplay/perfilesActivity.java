package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
 * Activity perfiles (acceso admin)
 * Activity que muestra todos los usuarios para que el admin pueda consultarlos
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */

public class perfilesActivity extends AppCompatActivity {

    //Elementos para el recyclerview
    RecyclerView recyclerViewVertical;
    ArrayList<String> nombreVertical, juegosVecinos, userVecinos, fotosVecinos, edadVecinos;
    ArrayList<Drawable> imagenesVecinos;
    RecyclerView.LayoutManager RecyclerViewLayoutManagerVertical;
    AdapterVecinos adapterVecinos;
    LinearLayoutManager verticalLayout;

    /**
     * Acciones a desarrollar al crear la Activity
     *
     * @param savedInstanceState Bundle (recursos Android) de la app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfiles);

        //elementos necesarios para comprobar que existe conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //comprobamos que existe conexion
        if (networkInfo != null && networkInfo.isConnected()) {

            //Reciclerview vertical//
            //////////////////////////
            recyclerViewVertical = (RecyclerView) findViewById(R.id.recyclerview_admin);
            RecyclerViewLayoutManagerVertical = new LinearLayoutManager(getApplicationContext());
            recyclerViewVertical.setLayoutManager(RecyclerViewLayoutManagerVertical);

            verticalLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerViewVertical.setLayoutManager(verticalLayout);

            try {
                obtenerTodos(this, Peticion.GET_TODOS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            String mensaje2 = "No dispone de conexión a internet. \nSpeak and Play necesita conexión a internet para funcionar. \nIntentelo de nuevo más tarde";
            Toast.makeText(perfilesActivity.this, mensaje2, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(perfilesActivity.this, MainActivity.class);

            startActivity(intent);
        }
    }


    /**
     * Realiza la peticion a la url aportada y devuelve todos los usuarios
     * @param context contexto app
     * @param url direccion del web service desde donde obtenemos los valores de la consulta
     * @throws JSONException debemos atrapar un error en caso de que el JSON obtenido desde el servidor no sea correcto o no obtengamos un JSON
     */
    public void obtenerTodos(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        //petición singleton mediante la libreria Volley
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // pasamos lo obtenido al procesador
                        procesarTodos(response);


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
    private void procesarTodos(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    //obtenemos el objeto info
                    JSONArray mensaje = response.getJSONArray("info");


                    nombreVertical = new ArrayList<>();
                    userVecinos = new ArrayList<>();
                    juegosVecinos = new ArrayList<>();
                    imagenesVecinos = new ArrayList<>();
                    edadVecinos = new ArrayList<>();
                    fotosVecinos = new ArrayList<>();

                    //Recorremos el JSONArray obteniendo los valores y cargandolos en el reciclerview
                    for (int i = 0; i < mensaje.length(); i++) {
                        try {
                            JSONObject jsonObject = mensaje.getJSONObject(i);

                            int edad = Integer.valueOf(jsonObject.getString("Edad"));

                            String nombreVecino = jsonObject.getString("Nombre") + " " + jsonObject.getString("Apellidos");
                            System.out.println(jsonObject.getString("Nombre") + " " + jsonObject.getString("Apellidos"));
                            nombreVertical.add(nombreVecino);
                            String usuarioVecino = jsonObject.getString("Usuainf");
                            userVecinos.add(usuarioVecino);
                            String fotoVecino = "a" + jsonObject.getString("foto");

                            fotosVecinos.add(fotoVecino);
                            Context context = getBaseContext();
                            int id = context.getResources().getIdentifier(fotoVecino, "drawable", context.getPackageName());
                            imagenesVecinos.add(getDrawable(id));
                            String juegoVecinoNombre = jsonObject.getString("Juego");
                            juegosVecinos.add(juegoVecinoNombre);
                            System.out.println("edad " + edad);
                            edadVecinos.add(String.valueOf(edad));


                            // añadir elementos al adaptador
                            adapterVecinos = new AdapterVecinos(nombreVertical, imagenesVecinos, juegosVecinos, userVecinos);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    //Cuando hacemos clic sobre un elemento abrimos la actividad donde se muestra el perfil
                    adapterVecinos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String userSeleccinado = adapterVecinos.getUser(recyclerViewVertical.getChildAdapterPosition(view));


                            Intent intent = new Intent(perfilesActivity.this, PerfilActivity.class);

                            intent.putExtra("usuario", userSeleccinado);

                            intent.putExtra("perfil", "personal");

                            startActivity(intent);

                        }
                    });
                    // añadir elementos del adapter
                    recyclerViewVertical.setAdapter(adapterVecinos);
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(perfilesActivity.this, mensaje2, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}