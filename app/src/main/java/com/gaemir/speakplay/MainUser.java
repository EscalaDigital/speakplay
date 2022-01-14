package com.gaemir.speakplay;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainUser extends AppCompatActivity {

    private Toolbar toolbar;
    CircleImageView logoPerfil;

    //variable usuario para usar durante la conexión
    String usuario;
    public String foto;


    //Elementos para el recyclerview horizontal
    RecyclerView recyclerViewHorizontal;
    ArrayList<String> source;
    ArrayList<Drawable> imagenesAmigos;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    AdapterAmigos adapter;
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

        try {

            obtenerFoto(this, Peticion.GET_FOTO + "?user=" + usuario);

        } catch (JSONException e) {

            Context context = logoPerfil.getContext();
            int id = context.getResources().getIdentifier("a10", "drawable", context.getPackageName());
            logoPerfil.setImageResource(id);
            toolbar.inflateMenu(R.menu.menu);
        }






        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        Log.i("", "Usuario: " + usuario);

        Log.i("", "Juego: " + pref.getString("juegos_preferences", ""));
        Log.i("", "Hombres: " + pref.getBoolean("check_box_hombres", true));
        Log.i("", "Mujeres: " + pref.getBoolean("check_box_mujeres", true));
        Log.i("", "Oculto: " + pref.getBoolean("check_box_oculto", true));
        Log.i("", "Mínimo: " + pref.getInt("seek_bar_minimo", 18));
        Log.i("", "Máximo: " + pref.getInt("seek_bar_maximo", 99));
        Log.i("", "KM: " + pref.getInt("seek_bar_distancia", 10));


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


                } else {
                    // do something
                }

                return false;
            }
        });


        // Añadir elementos al arraylist
        AddItemsToRecyclerViewArrayList();

        //Reciclerview Horizontal//
        //////////////////////////
        recyclerViewHorizontal = (RecyclerView) findViewById(R.id.recyclerview_horizontal);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHorizontal.setLayoutManager(RecyclerViewLayoutManager);


        // añadir elementos al adaptador
        adapter = new AdapterAmigos(source, imagenesAmigos);

        //cargar el layout de forma horizontal

        HorizontalLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHorizontal.setLayoutManager(HorizontalLayout);

        // añador elementos del adapter
        recyclerViewHorizontal.setAdapter(adapter);


        //Reciclerview vertical//
        //////////////////////////
        recyclerViewVertical = (RecyclerView) findViewById(R.id.recyclerview_vertical);
        RecyclerViewLayoutManagerVertical = new LinearLayoutManager(getApplicationContext());
        recyclerViewVertical.setLayoutManager(RecyclerViewLayoutManagerVertical);

        // añadir elementos al adaptador
        adapterVecinos = new AdapterVecinos(nombreVertical, imagenesVecinos, juegosVecinos);

        //cargar el layout de forma horizontal

        verticalLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewVertical.setLayoutManager(verticalLayout);

        // añador elementos del adapter
        recyclerViewVertical.setAdapter(adapterVecinos);
    }

    // Function to add items in RecyclerView.
    public void AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList
        source = new ArrayList<>();
        source.add("RedFire");
        source.add("Starman");
        source.add("Gaemir");
        source.add("Holilad");
        source.add("Dangerman");
        source.add("Lolipower");
        source.add("Goku");

        imagenesAmigos = new ArrayList<>();
        imagenesAmigos.add(getDrawable(R.drawable.a2));
        imagenesAmigos.add(getDrawable(R.drawable.a3));
        imagenesAmigos.add(getDrawable(R.drawable.a4));
        imagenesAmigos.add(getDrawable(R.drawable.a5));
        imagenesAmigos.add(getDrawable(R.drawable.a6));
        imagenesAmigos.add(getDrawable(R.drawable.a7));
        imagenesAmigos.add(getDrawable(R.drawable.a8));

        nombreVertical = new ArrayList<>();
        nombreVertical.add("RedFire");
        nombreVertical.add("Starman");
        nombreVertical.add("Gaemir");
        nombreVertical.add("Holilad");
        nombreVertical.add("Dangerman");
        nombreVertical.add("Lolipower");
        nombreVertical.add("Goku");

        imagenesVecinos = new ArrayList<>();
        imagenesVecinos.add(getDrawable(R.drawable.a2));
        imagenesVecinos.add(getDrawable(R.drawable.a3));
        imagenesVecinos.add(getDrawable(R.drawable.a4));
        imagenesVecinos.add(getDrawable(R.drawable.a5));
        imagenesVecinos.add(getDrawable(R.drawable.a6));
        imagenesVecinos.add(getDrawable(R.drawable.a7));
        imagenesVecinos.add(getDrawable(R.drawable.a8));

        juegosVecinos = new ArrayList<>();
        juegosVecinos.add("RedFire");
        juegosVecinos.add("Starman");
        juegosVecinos.add("Gaemir");
        juegosVecinos.add("Holilad");
        juegosVecinos.add("Dangerman");
        juegosVecinos.add("Lolipower");
        juegosVecinos.add("Goku");


    }

    /**
     * Realiza la peticion a la url aportada y devuelve un codigo de imagen
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
                    break;
                case "2": // FALLIDO
                    foto =  "a11";
                    break;

                case "3": // FALLIDO
                    foto =  "a12";
                    break;
            }

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


}

