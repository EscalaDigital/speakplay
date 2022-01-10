package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    ImageView botonRegistro;
    EditText usuario, clave, nombre, apellidos, email, edad, usuarioDiscord;
    RadioGroup sexoGrupo;
    RadioButton sexo;
    CheckBox terminos;

    Spinner spinner;

    Map<String,Integer> controlJuego = new HashMap<String,Integer>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


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


        this.botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        int numeroSexo = 2, numeroEdad;
                        String textoUsuario, textoClave, textoNombre, textoApellidos, TextoEmail, textousuarioDiscord, textoJuego;

                        textoUsuario = usuario.getText().toString();
                        textoClave = clave.getText().toString();
                        textoNombre = nombre.getText().toString();
                        textoApellidos = apellidos.getText().toString();
                        TextoEmail = email.getText().toString();
                        numeroEdad = Integer.parseInt(edad.getText().toString());
                        textousuarioDiscord = usuarioDiscord.getText().toString();
                        textoJuego = spinner.getSelectedItem().toString();

                        int id_juego = controlJuego.get(textoJuego);

                        int selectedId = sexoGrupo.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        sexo = (RadioButton) findViewById(selectedId);

                        if (sexo.getText().toString().matches("Hombre")) {
                            numeroSexo = 0;
                        } else if (sexo.getText().toString().matches("Mujer")) {
                            numeroSexo = 1;
                        } else {
                            numeroSexo = 2;
                        }



                        try {
                            System.err.println("Juego elegido: " + id_juego);
                            procesarPeticion(RegistroActivity.this, Peticion.INSERT_DATAUSER + "?user=" + textoUsuario + "&pass=" + textoClave + "&nombre=" + textoNombre + "&apellidos=" + textoApellidos + "&email=" + TextoEmail + "&edad=" + numeroEdad + "&sexo=" + numeroSexo + "&discord=" + textousuarioDiscord + "&juego=" + id_juego + "");


                        } catch (JSONException e) {
                            String mensaje = "Ocurrió un error inesperado, por favor inténtelo más tarde";
                            Toast.makeText(RegistroActivity.this, mensaje, Toast.LENGTH_LONG).show();
                        }


                    } else {
                        String mensaje = "Debe aceptar los terminos de uso";
                        Toast.makeText(RegistroActivity.this, mensaje, Toast.LENGTH_LONG).show();
                    }
                }
            }

        });
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

                    for (int i = 0; i < mensaje.length(); i++) {
                        try {
                            JSONObject jsonObject = mensaje.getJSONObject(i);
                            if (jsonObject.has("Juego")) {
                                String nombreJuego = jsonObject.getString("Juego");
                                int IDjuego = Integer.parseInt(jsonObject.getString("ID_juego"));
                                System.out.println(IDjuego);
                                controlJuego.put(nombreJuego,IDjuego);

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
     * Realiza la peticion a la url aportada y devuelve un objeto json
     */
    public void procesarPeticion(Context context, String url) throws JSONException {

        String TAG = getClass().getName();


        // Petición GET
        VolleySingleton.getInstance(context).addToRequestQueue(

                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta Json
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