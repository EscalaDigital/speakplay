package com.gaemir.speakplay;

import android.app.FragmentTransaction;
//import android.preference.PreferenceActivity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity acceso al fragment preferences
 * Clase activity que redirige al fragment para el acceso a los filtros de búsqueda
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */

public class FiltrosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }


}







