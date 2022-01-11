package com.gaemir.speakplay;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SettingsFragment extends PreferenceFragmentCompat {

    ListPreference listPreferenceCategory;
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.filtros);


       listPreferenceCategory  = (ListPreference) findPreference("juegos_preferences");

        try {
            solicitarJuegos(getActivity(), Peticion.GET_JUEGOS);

        } catch (JSONException e) {
            e.printStackTrace();
            System.err.println("Error al solicitar juegos");
        }

        CharSequence entries[] = new String[4];
        CharSequence entryValues[] = new String[4];

        entries[0] = "hola";
        entries[1] = "adios";
        entries[2] = "qjase";
        entries[3] = "Buenas noches";

        entryValues[0] = Integer.toString(1);
        entryValues[1] = Integer.toString(2);
        entryValues[2] = Integer.toString(3);
        entryValues[3] = Integer.toString(4);



        listPreferenceCategory.setEntries(entries);
        listPreferenceCategory.setEntryValues(entryValues);
    }

    /**
     * Realiza la peticion de juegos al servidor
     */
    public void solicitarJuegos(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json

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

                    JSONArray mensaje = response.getJSONArray("juegos");
                    ArrayList<String> strings = new ArrayList<>();


                    CharSequence entries[] = new String[mensaje.length()];
                    CharSequence entryValues[] = new String[mensaje.length()];








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