package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    String usuario;
    int distancia;
    double latitude, longitude;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        usuario = getIntent().getExtras().getString("usuario");
        distancia = getIntent().getExtras().getInt("distancia");
        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null && !(networkInfo.isConnected())) {

            String mensaje2 = "No dispone de conexión a internet. \nSpeak and Play necesita conexión a internet para funcionar. \nIntentelo de nuevo más tarde";
            Toast.makeText(MapaActivity.this, mensaje2, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(MapaActivity.this, MainActivity.class);

            startActivity(intent);
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null && !(networkInfo.isConnected())) {

            String mensaje2 = "No dispone de conexión a internet. \nSpeak and Play necesita conexión a internet para funcionar. \nIntentelo de nuevo más tarde";
            Toast.makeText(MapaActivity.this, mensaje2, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(MapaActivity.this, MainActivity.class);

            startActivity(intent);
        }

        this.googleMap = googleMap;
        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(latitude,longitude)).zoom(13).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        try {
            obtenerVecinos(this, Peticion.GET_UBICATIONS + "?user=" + usuario + "&distancia=" + distancia);
        } catch (JSONException e) {
            e.printStackTrace();
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
                            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(id);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(latitudeVecino,longitudeVecino)).title(nombre).icon(icon));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(MapaActivity.this, mensaje2, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}