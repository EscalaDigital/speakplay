package com.gaemir.speakplay;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Clase de filtros de búsqueda
 * Clase que contiene un fragment con los filtros (preferences) de busqueda de usuarios vecinos.
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */

public class SettingsFragment extends PreferenceFragmentCompat {


    ListPreference listPreferenceCategory;


    /**
     * Acciones a desarrollar al crear la Activity
     *
     * @param bundle Bundle (recursos Android) de la app
     * @param s variable String (no usada en este código)
     */
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.filtros);


        listPreferenceCategory = (ListPreference) findPreference("juegos_preferences");

        try {
            //cargamos los juegos desde el servidos
            solicitarJuegos(getActivity(), Peticion.GET_JUEGOS);

        } catch (JSONException e) {
            e.printStackTrace();
            System.err.println("Error al solicitar juegos");
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
                    //Carga elemetos de juegos
                    JSONArray mensaje = response.getJSONArray("juegos");
                    ArrayList<String> strings = new ArrayList<>();


                    //Recipientes para los valores de juego (nombre y codigo)
                    CharSequence entries[] = new String[mensaje.length()];
                    CharSequence entryValues[] = new String[mensaje.length()];


                    //Recorremos los valores y los vamos insertando
                    for (int i = 0; i < mensaje.length(); i++) {
                        try {
                            JSONObject jsonObject = mensaje.getJSONObject(i);
                            if (jsonObject.has("Juego")) {
                                String nombreJuego = jsonObject.getString("Juego");
                                int IDjuego = Integer.parseInt(jsonObject.getString("ID_juego"));
                                entries[i] = nombreJuego;
                                entryValues[i] = Integer.toString(IDjuego);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //Pasamos los nombres de los juegos y sus códigos
                    listPreferenceCategory.setEntries(entries);
                    listPreferenceCategory.setEntryValues(entryValues);


                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(getActivity(), mensaje2, Toast.LENGTH_LONG).show();
                    break;

                case "3": // FALLIDO
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(getActivity(), mensaje3, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}