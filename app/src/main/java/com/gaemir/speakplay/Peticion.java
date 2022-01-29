package com.gaemir.speakplay;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase con urls
 * URLs de acceso al web service de consultas
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */
public class Peticion {

    /**
     * Urls a las consultas
     */

    public static final String LOGIN = "https://escaladigital.net/speakandplay/consultar_user.php";
    public static final String INSERT_DATAUSER = "https://escaladigital.net/speakandplay/insert_datauser.php";
    public static final String GET_JUEGOS = "https://escaladigital.net/speakandplay/obtener_juegos.php";
    public static final String GET_FOTO = "https://escaladigital.net/speakandplay/obtener_foto.php";
    public static final String GET_INFO = "https://escaladigital.net/speakandplay/obtener_info.php";
    public static final String GET_RELATIONS = "https://escaladigital.net/speakandplay/obtener_relaciones.php";
    public static final String GET_UBICATIONS = "https://escaladigital.net/speakandplay/obtener_ubicaciones.php";
    public static final String DENEGAR_AMISTAD = "https://escaladigital.net/speakandplay/rechazar_amistad.php";
    public static final String SOLICITAR_AMISTAD = "https://escaladigital.net/speakandplay/insert_amistad.php";
    public static final String GET_TODOS = "https://escaladigital.net/speakandplay/obtener_todos.php";
    public static final String GET_ESTADISTICAS = "https://escaladigital.net/speakandplay/obtener_estadisticas.php";


}
