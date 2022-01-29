package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity mapa acceso administrador
 * Acceso al mapa, solo para el administrador. Realiza la búsqueda de todos los usuarios y los muestra a escala nacional
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */
public class mapaAdminActivity extends AppCompatActivity implements OnMapReadyCallback {

    double latitude, longitude;
    GoogleMap googleMap;

    /**
     * Acciones a desarrollar al crear la Activity
     *
     * @param savedInstanceState Bundle (recursos Android) de la app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_admin);
        //elementos necesarios para cargar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //elementos necesarios para comprobar que existe conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null && !(networkInfo.isConnected())) {

            String mensaje2 = "No dispone de conexión a internet. \nSpeak and Play necesita conexión a internet para funcionar. \nIntentelo de nuevo más tarde";
            Toast.makeText(mapaAdminActivity.this, mensaje2, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(mapaAdminActivity.this, MainActivity.class);

            startActivity(intent);
        }


    }

    /**
     * Otrapamos la creación del mapa
     * @param googleMap Objeto Googlemap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //elementos necesarios para comprobar que existe conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null && !(networkInfo.isConnected())) {

            String mensaje2 = "No dispone de conexión a internet. \nSpeak and Play necesita conexión a internet para funcionar. \nIntentelo de nuevo más tarde";
            Toast.makeText(mapaAdminActivity.this, mensaje2, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(mapaAdminActivity.this, MainActivity.class);

            startActivity(intent);
        }


        //llamamos elementos google maps y le pasamos los valores de ubicacion de usuarios
        this.googleMap = googleMap;
        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(40.4165, -3.70256)).zoom(6).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        try {
            obtenerVecinos(this, Peticion.GET_TODOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Realiza la peticion a la url aportada y devuelve todos los usuarios
     * @param context contexto app
     * @param url direccion del web service desde donde obtenemos los valores de la consulta
     * @throws JSONException debemos atrapar un error en caso de que el JSON obtenido desde el servidor no sea correcto o no obtengamos un JSON
     */
    public void obtenerVecinos(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        //petición singleton mediante la libreria Volley
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
                    //obtenemos el objeto info
                    JSONArray mensaje = response.getJSONArray("info");

                    //Recorremos el JSONArray obteniendo los valores y cargandolos en el mapa
                    for (int i = 0; i < mensaje.length(); i++) {
                        try {
                            JSONObject jsonObject = mensaje.getJSONObject(i);


                            double latitudeVecino = Double.valueOf(jsonObject.getString("latitude"));
                            double longitudeVecino = Double.valueOf(jsonObject.getString("longitude"));
                            System.out.println(latitude + "     " + longitude);
                            String nombre = jsonObject.getString("Nombre");
                            String fotoVecino = "a" + jsonObject.getString("foto");
                            Context context = getBaseContext();
                            int id = context.getResources().getIdentifier(fotoVecino, "drawable", context.getPackageName());
                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(id);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(latitudeVecino, longitudeVecino)).title(nombre).icon(icon));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(mapaAdminActivity.this, mensaje2, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}