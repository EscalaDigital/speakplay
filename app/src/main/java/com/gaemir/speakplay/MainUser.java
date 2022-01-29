package com.gaemir.speakplay;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Activity principal de acceso de usuarios.
 * Pantalla principal tras el login. En ella se muestran las personas cercanas al usuario (segun sus parametros de búsqueda), las persoans con las que conecta. El acceso al prefil, a los filtros de búsqueda y al mapa
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */
public class MainUser extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Toolbar toolbar;
    CircleImageView logoPerfil;

    //variable usuario para usar durante la conexión
    String usuario, juegoUsuario;
    double latitude, longitude;

    //variables obtenidas de los filtros
    int distancia, edadMinima, edadMaxima;
    String juegoSeleccionado;
    boolean hombreSelec, mujerSelec, sexoSelec;
    SharedPreferences pref;


    //recopilacion vecinos
    Vecino vecino;


    //Elementos para el recyclerview horizontal
    RecyclerView recyclerViewHorizontal;
    ArrayList<String> nombreAmigos, userAmigos, nombreReal;
    ArrayList<Integer> tipoAmistad, edadAmigo;
    ArrayList<Drawable> imagenesAmigos;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    AdapterAmigos adapterAmigos;
    LinearLayoutManager HorizontalLayout;


    //Elementos para el recyclerview vertical
    RecyclerView recyclerViewVertical;
    ArrayList<String> nombreVertical, juegosVecinos, userVecinos, fotosVecinos, edadVecinos;

    ArrayList<Drawable> imagenesVecinos;
    ArrayList<Vecino> vecinos;
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
        setContentView(R.layout.principal_user);
        //nombre usuario
        usuario = getIntent().getExtras().getString("usuario");

        //tomamos la imagen y la insertamos en la toolbar
        logoPerfil = findViewById(R.id.imagenPerfil);
        toolbar = findViewById(R.id.toolbarPrincipal);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.filtros, true);

        //Obtenemos las preferences del usuario
        hombreSelec = pref.getBoolean("check_box_hombres", true);
        mujerSelec = pref.getBoolean("check_box_mujeres", true);
        sexoSelec = pref.getBoolean("check_box_oculto", true);
        edadMinima = pref.getInt("seek_bar_minimo", 30);
        edadMaxima = pref.getInt("seek_bar_maximo", 99);
        distancia = (pref.getInt("seek_bar_distancia", 50)) * 1000;

        //elementos necesarios para comprobar la conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //comprobamos conexión
        if (networkInfo != null && networkInfo.isConnected()) {
            try {

                obtenerFoto(this, Peticion.GET_FOTO + "?user=" + usuario);

            } catch (JSONException e) {
                //si no consigue la imagen desde el servidor le añadimos una común
                Context context = logoPerfil.getContext();
                int id = context.getResources().getIdentifier("a10", "drawable", context.getPackageName());
                logoPerfil.setImageResource(id);
                toolbar.inflateMenu(R.menu.menu);
            }

            //Al pulsar la foto de perfil (arriba a la izquierda) se abre la pantalla perfil de usuario
            this.logoPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainUser.this, PerfilActivity.class);

                    intent.putExtra("usuario", usuario);

                    intent.putExtra("perfil", "personal");

                    startActivity(intent);

                }
            });

            //Acceso al menu desplegable
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //acceso a los filtros
                    if (item.getItemId() == R.id.menufiltros) {
                        startActivity(new Intent(MainUser.this, FiltrosActivity.class));

                        //acceso al mapa
                    } else if (item.getItemId() == R.id.menumapa) {

                        Intent intent = new Intent(MainUser.this, MapaActivity.class);

                        intent.putExtra("usuario", usuario);

                        intent.putExtra("distancia", distancia);

                        intent.putExtra("latitude", latitude);

                        intent.putExtra("longitude", longitude);

                        startActivity(intent);

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

        } else {
            String mensaje2 = "No dispone de conexión a internet. \nSpeak and Play necesita conexión a internet para funcionar. \nIntentelo de nuevo más tarde";
            Toast.makeText(MainUser.this, mensaje2, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(MainUser.this, MainActivity.class);

            startActivity(intent);
        }


    }

    /**
     * En onResume obtenenemos las actualizaciones de las preferencias y si existen cambios se realizan sobre vecinos y actualizamos amigos
     */
    @Override
    public void onResume() {
        super.onResume();
        //obtenenemos las actualizaciones de las preferencias
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener(this);
        try {
            obtenerAmigos(this, Peticion.GET_RELATIONS + "?user=" + usuario + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * si se realiza un cambio en las preferencias (filtros) se refresca el reciclerview
     */

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        finish();
        startActivity(getIntent());

    }


    /**
     * Realiza la peticion a la url aportada y devuelve un codigo de imagen que se inserta arriba a la izquierda
     * @param context contexto app
     * @param url direccion del web service desde donde obtenemos los valores de la consulta
     * @throws JSONException debemos atrapar un error en caso de que el JSON obtenido desde el servidor no sea correcto o no obtengamos un JSON
     */
    public void obtenerFoto(Context context, String url) throws JSONException {

        String TAG = getClass().getName();
        //petición singleton mediante la libreria Volley
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // pasamos lo obtenido al procesador
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
                   //obtenemos el objeto info
                    JSONObject object = response.getJSONObject("info");
                    foto = "a" + object.getString("foto");
                    //obtenemos valores del usuario de la app
                    juegoUsuario = object.getString("ID_juego");
                    latitude = Double.valueOf(object.getString("latitude"));
                    longitude = Double.valueOf(object.getString("longitude"));


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

            //Pasamos foto a perfil
            Context context = logoPerfil.getContext();
            int id = context.getResources().getIdentifier(foto, "drawable", context.getPackageName());
            logoPerfil.setImageResource(id);
            toolbar.inflateMenu(R.menu.menu);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Realiza la peticion a la url aportada y devuelve todos los usuarios dentro de la distancia establecida
     * @param context contexto app
     * @param url direccion del web service desde donde obtenemos los valores de la consulta
     * @throws JSONException debemos atrapar un error en caso de que el JSON obtenido desde el servidor no sea correcto o no obtengamos un JSON
     */
    public void obtenerVecinos(Context context, String url) throws JSONException {

        String TAG = getClass().getName();
        //petición singleton mediante la libreria Volley
        VolleySingleton.getInstance(context).addToRequestQueue(
                //obtenemos la respuesta
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
     * Interpreta los resultados de la respuesta
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
                    //obtenemso valores de info
                    JSONArray mensaje = response.getJSONArray("info");

                    //Listas de valores para pasar al Adapter de vecinos (AdapterVecinos)
                    nombreVertical = new ArrayList<>();
                    userVecinos = new ArrayList<>();
                    juegosVecinos = new ArrayList<>();
                    imagenesVecinos = new ArrayList<>();
                    vecinos = new ArrayList<>();
                    edadVecinos = new ArrayList<>();
                    fotosVecinos = new ArrayList<>();


                    for (int i = 0; i < mensaje.length(); i++) {
                        try {
                            JSONObject jsonObject = mensaje.getJSONObject(i);

                            //obtenemos valores del array
                            int edad = Integer.valueOf(jsonObject.getString("Edad"));
                            int sexo = Integer.valueOf(jsonObject.getString("Sexo"));
                            String juegoVecino = jsonObject.getString("ID_juego");


                            if (juegoSeleccionado.equals(juegoVecino)) {
                                if ((hombreSelec && sexo == 0) || (mujerSelec && sexo == 1) || (sexoSelec && sexo == 2)) {
                                    if (edadMinima <= edad && edadMaxima >= edad) {

                                        String nombreVecino = jsonObject.getString("Nombre") + " " + jsonObject.getString("Apellidos");
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


                                    }
                                }
                            }


                            // añadir elementos al adaptador
                            adapterVecinos = new AdapterVecinos(nombreVertical, imagenesVecinos, juegosVecinos, userVecinos);

                            vecino = new Vecino(nombreVertical, userVecinos, fotosVecinos, juegosVecinos, edadVecinos);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    //Accion para cuando se pulsa sobre un elemento del reciclerview
                    adapterVecinos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(MainUser.this, VecinoMain.class);

                            intent.putExtra("usuario", usuario);
                            intent.putExtra("seleccion", recyclerViewVertical.getChildAdapterPosition(view));
                            intent.putExtra("vecinos", vecino);
                            intent.putExtra("relacion", "busqueda");


                            startActivity(intent);
                            //

                        }
                    });
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
     * @param context contexto app
     * @param url direccion del web service desde donde obtenemos los valores de la consulta
     * @throws JSONException debemos atrapar un error en caso de que el JSON obtenido desde el servidor no sea correcto o no obtengamos un JSON
     */
    public void obtenerAmigos(Context context, String url) throws JSONException {

        String TAG = getClass().getName();
        //petición singleton mediante la libreria Volley
        VolleySingleton.getInstance(context).addToRequestQueue(
                //obtenemos la respuesta
                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // pasamos lo obtenido al  procesador
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

                    //obtenemos valores de info
                    JSONArray mensajeAmigos = response.getJSONArray("info");

                    nombreAmigos = new ArrayList<>();
                    userAmigos = new ArrayList<>();
                    imagenesAmigos = new ArrayList<>();
                    tipoAmistad = new ArrayList<>();
                    nombreReal = new ArrayList<>();
                    edadAmigo = new ArrayList<>();

                    //Recorremos array obteniendo valores y cargandolos al adaptador
                    for (int i = 0; i < mensajeAmigos.length(); i++) {

                        try {
                            JSONObject jsonObjectAmigos = mensajeAmigos.getJSONObject(i);

                            int tipoAmigo = jsonObjectAmigos.getInt("tipo");

                            if (tipoAmigo <= 1) {
                                tipoAmistad.add(tipoAmigo);

                                String nombreAmigo = jsonObjectAmigos.getString("Nickdiscord");
                                nombreAmigos.add(nombreAmigo);
                                String fotoAmigo = "a" + jsonObjectAmigos.getString("foto");
                                String usuarioAmigo = jsonObjectAmigos.getString("Usuainf");
                                userAmigos.add(usuarioAmigo);
                                Context context = getBaseContext();
                                int id = context.getResources().getIdentifier(fotoAmigo, "drawable", context.getPackageName());
                                imagenesAmigos.add(getDrawable(id));
                                String nombreAmigoReal = jsonObjectAmigos.getString("nombre");
                                nombreReal.add(nombreAmigoReal);
                                int edadAmigoObtenida = jsonObjectAmigos.getInt("edad");
                                edadAmigo.add(edadAmigoObtenida);


                                // añadir elementos al adaptador
                                adapterAmigos = new AdapterAmigos(nombreAmigos, userAmigos, imagenesAmigos, tipoAmistad, nombreReal, edadAmigo);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    //Atrapamos la acción clic sobre un elemento del reciclerview
                    adapterAmigos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String userSeleccinado = adapterAmigos.getUser(recyclerViewHorizontal.getChildAdapterPosition(view));

                            int tipoSeleccinado = adapterAmigos.getTipo(recyclerViewHorizontal.getChildAdapterPosition(view));

                            System.out.println(userSeleccinado);

                            //Según el tipo de relacion entre usuarios enviamos a una activity o a otra

                            //Cuando el que se pulsa es un amigo confirmado
                            if (tipoSeleccinado == 1) {

                                Intent intent = new Intent(MainUser.this, PerfilActivity.class);

                                intent.putExtra("usuario", userSeleccinado);

                                intent.putExtra("perfil", "externo");

                                startActivity(intent);

                                //Cuando el que se pulsa es un usuario que ha hecho una petición de amistad
                            } else if (tipoSeleccinado == 0) {
                                Intent intent = new Intent(MainUser.this, VecinoMain.class);

                                String nombreSeleccionado = adapterAmigos.getNombre(recyclerViewHorizontal.getChildAdapterPosition(view));
                                String userVecino = adapterAmigos.getUser(recyclerViewHorizontal.getChildAdapterPosition(view));
                                int edadSeleccionado = adapterAmigos.getEdad(recyclerViewHorizontal.getChildAdapterPosition(view));

                                intent.putExtra("usuario", usuario);
                                intent.putExtra("nombre", nombreSeleccionado);
                                intent.putExtra("edad", edadSeleccionado);
                                intent.putExtra("userVecino", userVecino);

                                intent.putExtra("relacion", "solicitud");

                                startActivity(intent);

                            }

                        }
                    });

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

