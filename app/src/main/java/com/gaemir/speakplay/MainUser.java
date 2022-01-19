package com.gaemir.speakplay;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainUser extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Toolbar toolbar;
    CircleImageView logoPerfil;

    //variable usuario para usar durante la conexión
    String usuario, juegoUsuario;

    //variables obtenidas de los filtros
    int distancia, edadMinima, edadMaxima;
    String juegoSeleccionado;
    boolean hombreSelec, mujerSelec, sexoSelec;
    SharedPreferences pref;


    //Elementos para el recyclerview horizontal
    RecyclerView recyclerViewHorizontal;
    ArrayList<String> nombreAmigos;
    ArrayList<Drawable> imagenesAmigos;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    AdapterAmigos adapterAmigos;
    LinearLayoutManager HorizontalLayout;


    //Elementos para el recyclerview vertical
    RecyclerView recyclerViewVertical;
    ArrayList<String> nombreVertical, juegosVecinos;
    ArrayList<Drawable> imagenesVecinos;
    RecyclerView.LayoutManager RecyclerViewLayoutManagerVertical;
    AdapterVecinos adapterVecinos;
    LinearLayoutManager verticalLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_user);
        //nombre usuario
        usuario = getIntent().getExtras().getString("usuario");


        //tomamos la imagen y la insertamos en la toolbar

        logoPerfil = findViewById(R.id.imagenPerfil);
        toolbar = findViewById(R.id.toolbarPrincipal);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.filtros, true);


        hombreSelec = pref.getBoolean("check_box_hombres", true);
        mujerSelec = pref.getBoolean("check_box_mujeres", true);
        sexoSelec = pref.getBoolean("check_box_oculto", true);
        edadMinima = pref.getInt("seek_bar_minimo", 30);
        edadMaxima = pref.getInt("seek_bar_maximo", 99);
        distancia = (pref.getInt("seek_bar_distancia", 50)) * 1000;


        try {

            obtenerFoto(this, Peticion.GET_FOTO + "?user=" + usuario);

        } catch (JSONException e) {
            //si no consigue la imagen desde el servidor le añadimos una común
            Context context = logoPerfil.getContext();
            int id = context.getResources().getIdentifier("a10", "drawable", context.getPackageName());
            logoPerfil.setImageResource(id);
            toolbar.inflateMenu(R.menu.menu);
        }

        this.logoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainUser.this, PerfilActivity.class);

                intent.putExtra("usuario", usuario);

                startActivity(intent);

            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.menufiltros) {
                    startActivity(new Intent(MainUser.this, FiltrosActivity.class));
                } else if (item.getItemId() == R.id.menumapa) {
                    startActivity(new Intent(MainUser.this, MapaActivity.class));

                }

                return false;
            }
        });


        //Reciclerview Horizontal//
        //////////////////////////
        recyclerViewHorizontal = (RecyclerView) findViewById(R.id.recyclerview_horizontal);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHorizontal.setLayoutManager(RecyclerViewLayoutManager);
        HorizontalLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHorizontal.setLayoutManager(HorizontalLayout);

        try {
            obtenerAmigos(this, Peticion.GET_RELATIONS + "?user=" + usuario + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Reciclerview vertical//
        //////////////////////////
        recyclerViewVertical = (RecyclerView) findViewById(R.id.recyclerview_vertical);
        RecyclerViewLayoutManagerVertical = new LinearLayoutManager(getApplicationContext());
        recyclerViewVertical.setLayoutManager(RecyclerViewLayoutManagerVertical);

        verticalLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewVertical.setLayoutManager(verticalLayout);

        try {
            obtenerVecinos(this, Peticion.GET_UBICATIONS + "?user=" + usuario + "&distancia=" + distancia);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        //obtenenemos las actualizaciones de las preferencias
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener(this);
    }

    //si se realiza un cambio en las preferencias (filtros) se refresca el reciclerview
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        finish();
        startActivity(getIntent());

    }


    /**
     * Realiza la peticion a la url aportada y devuelve un codigo de imagen que se inserta arriba a la izquierda
     */
    public void obtenerFoto(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
                        procesarFoto(response);


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
    public void procesarFoto(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            String foto = "a10";
            switch (estado) {
                case "1": // EXITO
                    // Obtener objeto "meta"
                    JSONObject object = response.getJSONObject("info");
                    foto = "a" + object.getString("foto");
                    juegoUsuario = object.getString("ID_juego");


                    break;
                case "2": // FALLIDO
                    foto = "a11";
                    juegoUsuario = "1";
                    break;

                case "3": // FALLIDO
                    foto = "a12";
                    juegoUsuario = "1";
                    break;
            }

            juegoSeleccionado = pref.getString("juegos_preferences", juegoUsuario);


            Context context = logoPerfil.getContext();
            int id = context.getResources().getIdentifier(foto, "drawable", context.getPackageName());
            logoPerfil.setImageResource(id);
            toolbar.inflateMenu(R.menu.menu);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateUI(Location loc) {
        if (loc != null) {

        } else {

        }
    }


    /**
     * Realiza la peticion a la url aportada y devuelve todos los usuarios dentro de la distancia establecida
     */
    public void obtenerVecinos(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
                        procesarVecinos(response);


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
    private void procesarVecinos(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensaje = response.getJSONArray("info");

                    nombreVertical = new ArrayList<>();
                    juegosVecinos = new ArrayList<>();
                    imagenesVecinos = new ArrayList<>();


                    for (int i = 0; i < mensaje.length(); i++) {
                        try {
                            JSONObject jsonObject = mensaje.getJSONObject(i);


                            int edad = Integer.valueOf(jsonObject.getString("Edad"));
                            int sexo = Integer.valueOf(jsonObject.getString("Sexo"));
                            String juegoVecino = jsonObject.getString("ID_juego");


                            if (juegoSeleccionado.equals(juegoVecino)) {
                                if ((hombreSelec && sexo == 0) || (mujerSelec && sexo == 1) || (sexoSelec && sexo == 2)) {
                                    if (edadMinima <= edad && edadMaxima >= edad) {
                                        String nombreVecino = jsonObject.getString("Nombre") + " " + jsonObject.getString("Apellidos");
                                        nombreVertical.add(nombreVecino);
                                        String fotoVecino = "a" + jsonObject.getString("foto");
                                        Context context = getBaseContext();
                                        int id = context.getResources().getIdentifier(fotoVecino, "drawable", context.getPackageName());
                                        imagenesVecinos.add(getDrawable(id));
                                        String juegoVecinoNombre = jsonObject.getString("Juego");
                                        juegosVecinos.add(juegoVecinoNombre);
                                    }
                                }
                            }


                            // añadir elementos al adaptador
                            adapterVecinos = new AdapterVecinos(nombreVertical, imagenesVecinos, juegosVecinos);

                            //cargar el layout de forma horizontal


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                    // añador elementos del adapter
                    recyclerViewVertical.setAdapter(adapterVecinos);
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(MainUser.this, mensaje2, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Realiza la peticion a la url aportada y devuelve todos los usuarios con una relacion establecida
     */
    public void obtenerAmigos(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
                        procesarAmigos(response);


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
    private void procesarAmigos(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estadoAmigos = response.getString("estado");

            switch (estadoAmigos) {
                case "1": // EXITO

                    JSONArray mensajeAmigos = response.getJSONArray("info");

                    nombreAmigos = new ArrayList<>();
                    imagenesAmigos = new ArrayList<>();


                    for (int i = 0; i < mensajeAmigos.length(); i++) {

                        try {
                            JSONObject jsonObjectAmigos = mensajeAmigos.getJSONObject(i);


                            String nombreAmigo = jsonObjectAmigos.getString("Nickdiscord");

                            nombreAmigos.add(nombreAmigo);
                            String fotoAmigo = "a" + jsonObjectAmigos.getString("foto");
                            Context context = getBaseContext();
                            int id = context.getResources().getIdentifier(fotoAmigo, "drawable", context.getPackageName());
                            imagenesAmigos.add(getDrawable(id));


                            // añadir elementos al adaptador
                            adapterAmigos = new AdapterAmigos(nombreAmigos, imagenesAmigos);

                            //cargar el layout de forma horizontal


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                    // añador elementos del adapter
                    recyclerViewHorizontal.setAdapter(adapterAmigos);

                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(MainUser.this, mensaje2, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

