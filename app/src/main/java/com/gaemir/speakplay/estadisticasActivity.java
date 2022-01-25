package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class estadisticasActivity extends AppCompatActivity {

    PieChartView pieChartView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            PieChartView pieChartView = findViewById(R.id.chart);

            try {
                obtenerTodos(this, Peticion.GET_ESTADISTICAS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            String mensaje2 = "No dispone de conexión a internet. \nSpeak and Play necesita conexión a internet para funcionar. \nIntentelo de nuevo más tarde";
            Toast.makeText(estadisticasActivity.this, mensaje2, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(estadisticasActivity.this, MainActivity.class);

            startActivity(intent);
        }
    }


    /**
     * Realiza la peticion a la url aportada y devuelve todos los usuarios
     */
    public void obtenerTodos(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
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

                    JSONArray mensaje = response.getJSONArray("info");


                    List pieData = new ArrayList<>();
                    pieChartView = findViewById(R.id.chart);

                    for (int i = 0; i < mensaje.length(); i++) {
                        try {
                            JSONObject jsonObject = mensaje.getJSONObject(i);


                            int sexo = Integer.valueOf(jsonObject.getString("sexo"));
                            int total = Integer.valueOf(jsonObject.getString("sexosuma"));
                            System.out.println(sexo);

                            if (sexo == 0) {

                                pieData.add(new SliceValue(total, Color.BLUE).setLabel("Hombres"));
                                /**    pieData.add(new SliceValue(25, Color.GRAY).setLabel("Q2: $4"));
                                 pieData.add(new SliceValue(10, Color.RED).setLabel("Q3: $18"));
                                 pieData.add(new SliceValue(60, Color.MAGENTA).setLabel("Q4: $28"));*/


                            } else if (sexo == 1) {
                                pieData.add(new SliceValue(total, Color.RED).setLabel("Mujeres"));
                            } else if (sexo == 2) {
                                pieData.add(new SliceValue(total, Color.MAGENTA).setLabel("Sexo oculto"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        PieChartData pieChartData = new PieChartData(pieData);
                        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                        pieChartData.setHasCenterCircle(true).setCenterText1("Usuarios por sexo").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                        pieChartView.setPieChartData(pieChartData);
                    }
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(estadisticasActivity.this, mensaje2, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}








