package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class estadisticasActivity extends AppCompatActivity {

    PieChartView graficoSexo, graficoJuegos;
    ColumnChartView graficoEdad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {


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

                    JSONArray mensajeSexo = response.getJSONArray("sexo");
                    JSONArray mensajeEdad = response.getJSONArray("edad");
                    JSONArray mensajeJuegos = response.getJSONArray("juegos");


                    List pieData = new ArrayList<>();
                    List pieDataJuegos = new ArrayList<>();
                    List<Column> pieDataEdad = new ArrayList<>();
                    List<SubcolumnValue> subcolumnValueList;


                    graficoSexo = findViewById(R.id.chart);
                    graficoJuegos = findViewById(R.id.chartJuegos);
                    graficoEdad = (ColumnChartView) findViewById(R.id.chartedad);

                    for (int i = 0; i < mensajeSexo.length(); i++) {
                        try {
                            JSONObject jsonObject = mensajeSexo.getJSONObject(i);


                            int sexo = Integer.valueOf(jsonObject.getString("sexo"));
                            int total = Integer.valueOf(jsonObject.getString("sexosuma"));
                            System.out.println(sexo);

                            if (sexo == 0) {

                                pieData.add(new SliceValue(total, Color.BLUE).setLabel("Hombres"));



                            } else if (sexo == 1) {
                                pieData.add(new SliceValue(total, Color.BLUE).setLabel("Mujeres"));
                            } else if (sexo == 2) {
                                pieData.add(new SliceValue(total, Color.BLUE).setLabel("Sexo oculto"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        PieChartData pieChartData = new PieChartData(pieData);
                        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                        pieChartData.setHasCenterCircle(true).setCenterText1("Usuarios por sexo").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                        graficoSexo.setPieChartData(pieChartData);
                    }

                    for (int i = 0; i < mensajeEdad.length(); i++) {
                        try {
                            JSONObject jsonObject = mensajeEdad.getJSONObject(i);


                            int meno10 = Integer.valueOf(jsonObject.getString("menor10"));
                            int entre1020 = Integer.valueOf(jsonObject.getString("entre1020"));
                            int entre2030 = Integer.valueOf(jsonObject.getString("entre2030"));
                            int entre3040 = Integer.valueOf(jsonObject.getString("entre3040"));
                            int entre4050 = Integer.valueOf(jsonObject.getString("entre4050"));
                            int entre5060 = Integer.valueOf(jsonObject.getString("entre5060"));
                            int entre6070 = Integer.valueOf(jsonObject.getString("entre6070"));
                            int entre7080 = Integer.valueOf(jsonObject.getString("entre7080"));
                            int entre8090 = Integer.valueOf(jsonObject.getString("entre8090"));
                            int entre90100 = Integer.valueOf(jsonObject.getString("entre90100"));

                            subcolumnValueList = new ArrayList<>();

                            subcolumnValueList.add(new SubcolumnValue(meno10, Color.BLUE).setLabel("Menor 10"));
                            subcolumnValueList.add(new SubcolumnValue(entre1020, Color.BLUE).setLabel("10 < 20"));
                            subcolumnValueList.add(new SubcolumnValue(entre2030, Color.BLUE).setLabel("20 < 30"));
                            subcolumnValueList.add(new SubcolumnValue(entre3040, Color.BLUE).setLabel("30 < 40"));
                            subcolumnValueList.add(new SubcolumnValue(entre4050, Color.BLUE).setLabel("40 < 50"));
                            subcolumnValueList.add(new SubcolumnValue(entre5060, Color.BLUE).setLabel("50 < 60"));
                            subcolumnValueList.add(new SubcolumnValue(entre6070, Color.BLUE).setLabel("60 < 70"));
                            subcolumnValueList.add(new SubcolumnValue(entre7080, Color.BLUE).setLabel("70 < 80"));
                            subcolumnValueList.add(new SubcolumnValue(entre8090, Color.BLUE).setLabel("80 < 90"));
                            subcolumnValueList.add(new SubcolumnValue(entre90100, Color.BLUE).setLabel("90 < 100"));


                            Column column = new Column(subcolumnValueList);
                            column.setHasLabels(true);
                            pieDataEdad.add(column);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                    Axis axisX = new Axis();
                    Axis axisY = new Axis();
                    ArrayList<AxisValue> axisValuesY = new ArrayList<AxisValue>();
                    //Set vertical axis name
                    List<AxisValue> axisValuesX = new ArrayList<>();

                    axisValuesX.add(new AxisValue(0).setLabel(""));
                    axisValuesY.add(new AxisValue(0).setLabel(""));

                    axisX.setValues(axisValuesX);

                    axisX.setName("Tramos de edad");    //Set horizontal axis name
                    axisY.setName("Personas");
                    axisX.setHasLines(true);
                    axisX.setTextSize(10);
                    axisY.setLineColor(Color.BLACK);
                    axisX.setTextColor(Color.BLACK);
                    axisY.setTextColor(Color.BLACK);
                    axisY.setLineColor(Color.BLACK);
                    axisY.setTextSize(10);

                    ColumnChartData graficoedad = new ColumnChartData(pieDataEdad);
                    graficoedad.setAxisXBottom(axisX); //Setting horizontal axis
                    graficoedad.setAxisYLeft(axisY);   //Setting up vertical shaft

                    graficoEdad.setColumnChartData(graficoedad);

                    for (int i = 0; i < mensajeJuegos.length(); i++) {
                        try {
                            JSONObject jsonObject = mensajeJuegos.getJSONObject(i);

                            String juego = jsonObject.getString("juego");
                            int totalusuarios = Integer.valueOf(jsonObject.getString("usuarios"));


                                pieDataJuegos.add(new SliceValue(totalusuarios, Color.BLUE).setLabel(juego));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        PieChartData pieChartDataJuegos = new PieChartData(pieDataJuegos);
                        pieChartDataJuegos.setHasLabels(true).setValueLabelTextSize(14);
                        pieChartDataJuegos.setHasCenterCircle(true).setCenterText1("Nº de usuarios").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                        graficoJuegos.setPieChartData(pieChartDataJuegos);
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








