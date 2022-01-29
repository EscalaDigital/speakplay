package com.gaemir.speakplay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import android.os.Looper;
import android.provider.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity de registro de usuarios
 * Clase activity para el registro de usuarios (formulario que recopila los datos y los inserta en el servidor)
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */

public class RegistroActivity extends AppCompatActivity {

    ImageView botonRegistro;
    EditText usuario, clave, nombre, apellidos, email, edad, usuarioDiscord;
    RadioGroup sexoGrupo;
    RadioButton sexo;
    CheckBox terminos;

    //elementos para cargar la lista de juegos desde la BD
    Spinner spinner;

    Map<String, Integer> controlJuego = new HashMap<String, Integer>();


    //Elementos para solicitar la ubicacion del usuario
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    String latitud, longit;

    /**
     * Acciones a desarrollar al crear la Activity
     *
     * @param savedInstanceState Bundle (recursos Android) de la app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);



        latitud = null;
        longit = null;


        //Elementos de la vista
        botonRegistro = (ImageView) findViewById(R.id.botonRegistro);
        usuario = (EditText) findViewById(R.id.usuario);
        clave = (EditText) findViewById(R.id.clave);
        nombre = (EditText) findViewById(R.id.nombre);
        apellidos = (EditText) findViewById(R.id.apellidos);
        email = (EditText) findViewById(R.id.email);
        edad = (EditText) findViewById(R.id.edad);
        usuarioDiscord = (EditText) findViewById(R.id.usuarioDiscord);
        sexoGrupo = (RadioGroup) findViewById(R.id.grupoSexo);
        terminos = (CheckBox) findViewById(R.id.checkBox);
        spinner = (Spinner) findViewById(R.id.selectorJuegos);

        try {

            solicitarJuegos(this, Peticion.GET_JUEGOS);

        } catch (JSONException e) {
            e.printStackTrace();
            System.err.println("Error al solicitar juegos");
        }

        //Elemento cliente de localizacion, a partir de el obtenemos los parametros latitud - longitud
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atención");
        builder.setMessage("Speak and Play basa su funcionamiento en la localización de los usuarios. \n\nEs por ello que deberá aceptar los permisos de ubicación cuando se le soliciten, o no podrá registrarse.\n\nDichos permisos solo se le solicitarán durante el resgistro y no durante el resto del uso de la app.\n\nDisculpe las molestias");

        //Panel desde el que obtenemos permiso para obtener la ubicación del disposito
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // método para obtener la ubicación
                getLastLocation();


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


        this.botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    if (usuario.getText().toString().matches("")
                            || clave.getText().toString().matches("")
                            || nombre.getText().toString().matches("")
                            || apellidos.getText().toString().matches("")
                            || email.getText().toString().matches("")
                            || edad.getText().toString().matches("")
                            || usuarioDiscord.getText().toString().matches("")
                            || sexoGrupo.getCheckedRadioButtonId() == -1) {

                        String mensaje = "Por favor, rellene todos los campos";
                        Toast.makeText(RegistroActivity.this, mensaje, Toast.LENGTH_LONG).show();

                    } else {

                        if (terminos.isChecked()) {

                            if (latitud == null || longit == null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                                builder.setTitle("Atención");
                                builder.setMessage("No se ha podido obtener su ubicación. \nPor favor, compruebe que ha concedido los permisos necesarios.\nSi ha sido así, por favor intentelo más tarde, si no, acceda a los ajustes de su dispotivo");

                                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        // method to get the location
                                        getLastLocation();


                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {

                                int numeroSexo = 2, numeroEdad, avatar;
                                String textoUsuario, textoClave, textoNombre, textoApellidos, TextoEmail, textousuarioDiscord, textoJuego;

                                textoUsuario = usuario.getText().toString();
                                textoClave = clave.getText().toString();
                                textoNombre = nombre.getText().toString();
                                textoApellidos = apellidos.getText().toString();
                                TextoEmail = email.getText().toString();
                                numeroEdad = Integer.parseInt(edad.getText().toString());
                                textousuarioDiscord = usuarioDiscord.getText().toString();
                                textoJuego = spinner.getSelectedItem().toString();


                                //arreglamos posibles espacios en blanco
                                textoUsuario = textoUsuario.replaceAll(" ", "%20");
                                textoClave = textoClave.replaceAll(" ", "%20");
                                textoNombre = textoNombre.replaceAll(" ", "%20");
                                textoApellidos = textoApellidos.replaceAll(" ", "%20");
                                textousuarioDiscord = textousuarioDiscord.replaceAll(" ", "%20");


                                int id_juego = controlJuego.get(textoJuego);

                                int selectedId = sexoGrupo.getCheckedRadioButtonId();

                                // find the radiobutton by returned id
                                sexo = (RadioButton) findViewById(selectedId);

                                if (sexo.getText().toString().matches("Hombre")) {
                                    numeroSexo = 0;
                                    avatar = (int) (Math.random() * 9 + 1);
                                } else if (sexo.getText().toString().matches("Mujer")) {
                                    numeroSexo = 1;
                                    avatar = (int) (Math.random() * (14 - 10 + 1) + 10);
                                } else {
                                    numeroSexo = 2;
                                    avatar = 10;
                                }


                                try {
                                    System.err.println("Juego elegido: " + id_juego);
                                    procesarPeticion(RegistroActivity.this, Peticion.INSERT_DATAUSER + "?user=" + textoUsuario + "&pass=" + textoClave + "&nombre=" + textoNombre + "&apellidos=" + textoApellidos + "&email=" + TextoEmail + "&edad=" + numeroEdad + "&sexo=" + numeroSexo + "&discord=" + textousuarioDiscord + "&juego=" + id_juego + "&avatar=" + avatar + "&latitud=" + latitud + "&longitud=" + longit + "");


                                } catch (JSONException e) {
                                    String mensaje = "Ocurrió un error inesperado, por favor inténtelo más tarde";
                                    Toast.makeText(RegistroActivity.this, mensaje, Toast.LENGTH_LONG).show();
                                }

                            }
                        } else {
                            String mensaje = "Debe aceptar los terminos de uso";
                            Toast.makeText(RegistroActivity.this, mensaje, Toast.LENGTH_LONG).show();
                        }


                    }
                } else {
                    String mensaje2 = "No dispone de conexión a internet. \nSpeak and Play necesita conexión a internet para funcionar. \nIntentelo de nuevo más tarde";
                    Toast.makeText(RegistroActivity.this, mensaje2, Toast.LENGTH_LONG).show();
                }


            }

        });
    }

    /**
     * Métodos para obtener la ubicación del usuario
     *
     * Aclaración: El siguiente código se ha obtenido de la web
     * https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
     * Con añadidos personales
     */

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // Comprobar que se posee permisos para obtener la ubicacion
        if (checkPermissions()) {

            // comprobar si la ubicacion esta activada
            if (isLocationEnabled()) {

                // obtener la última ubicación
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitud = location.getLatitude() + "";
                            longit = location.getLongitude() + "";
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // Si no tenemos permiso, se solicita
            requestPermissions();
        }
    }

    /**
     * Obtener los valores de localización
     */
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // Los valores de vuelcan en una variable tipo FusedLocationProviderClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    /**
     * Obtiene valores y los pasa a variables latitud - longitud
     */
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitud = +mLastLocation.getLatitude() + "";
            longit = mLastLocation.getLongitude() + "";
        }
    };

    /**
     * Método para comprobar si poseemos permiso para obtener la ubicación
     * @return verdadero o falso con respecto a los permisos
     */
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Método para solicitar permisos para obtener la ubicación
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // Si la localización está activada devuelve verdadero
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Método que en caso de tener permisos devuelve la ultima localización
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    /**
     * Comprobamos permisos y obtenemos localizacion si volvemos sobre la activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


    /**
     * Realiza la peticion de juegos al servidor
     * @param context contexto app
     * @param url direccion del web service desde donde obtenemos los valores de la consulta
     * @throws JSONException debemos atrapar un error en caso de que el JSON obtenido desde el servidor no sea correcto o no obtengamos un JSON
     */
    public void solicitarJuegos(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        //petición singleton mediante la libreria Volley
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // pasamos lo obtenido al procesador

                        insertarJuegos(response);


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
     * Obtiene los juegos y los carga en el spinner
     *
     * @param response Objeto Json con la respuesta
     */
    public void insertarJuegos(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    //obtenemos el objeto juegos
                    JSONArray mensaje = response.getJSONArray("juegos");
                    ArrayList<String> strings = new ArrayList<>();

                    //Obtenemos los valores y los cargamos sobre el spinner
                    for (int i = 0; i < mensaje.length(); i++) {
                        try {
                            JSONObject jsonObject = mensaje.getJSONObject(i);
                            if (jsonObject.has("Juego")) {
                                String nombreJuego = jsonObject.getString("Juego");
                                int IDjuego = Integer.parseInt(jsonObject.getString("ID_juego"));
                                System.out.println(IDjuego);
                                controlJuego.put(nombreJuego, IDjuego);

                                strings.add(nombreJuego);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);


                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(this, mensaje2, Toast.LENGTH_LONG).show();
                    break;

                case "3": // FALLIDO
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(this, mensaje3, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Toma los valores del formulario, la ubicación y el juego del spinner y lo manda al servidor mediante url
     * @param context contexto app
     * @param url direccion del web service desde donde obtenemos los valores de la consulta
     * @throws JSONException debemos atrapar un error en caso de que el JSON obtenido desde el servidor no sea correcto o no obtengamos un JSON
     */
    public void procesarPeticion(Context context, String url) throws JSONException {

        String TAG = getClass().getName();

        //petición singleton mediante la libreria Volley
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // pasamos lo obtenido al procesador
                        procesarRespuesta(response);


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
    public void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    String mensaje = "Usuario creado con éxito";
                    Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegistroActivity.this, MainActivity.class));

                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(this, mensaje2, Toast.LENGTH_LONG).show();
                    break;

                case "3": // FALLIDO
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(this, mensaje3, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            String mensaje = "Problemas al crear el usuario";
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}