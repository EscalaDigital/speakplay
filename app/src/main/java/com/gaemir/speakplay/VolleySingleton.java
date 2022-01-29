package com.gaemir.speakplay;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Clase Volley
 * Esta clase utiliza las librerias volley para realizar consultas a la base de datos alojada en el servidor
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */

/**
 * Clase para conectar con la libreria Volley realizar las conexiones de una en una (Singlenton)
 */

public final class VolleySingleton {

    // Atributos
    private static VolleySingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;

    /**
     * Constructor de la clase
     * @param context contexto de la activity
     */
    private VolleySingleton(Context context) {
        VolleySingleton.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Retorna la instancia unica del singleton
     * @param context contexto donde se ejecutarán las peticiones
     * @return Instancia
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new VolleySingleton(context.getApplicationContext());
        }
        return singleton;
    }

    /**
     * Obtiene la instancia de la cola de peticiones
     * @return cola de peticiones
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Añade la petición a la cola
     * @param req petición
     * @param <T> Resultado final de tipo T
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}