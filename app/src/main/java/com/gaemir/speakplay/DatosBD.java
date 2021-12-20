package com.gaemir.speakplay;


import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatosBD {

   private String[] servidor = new String[4];
int control = 0;

    public DatosBD(Context context) {
        try {

            InputStream is = context.getResources().openRawResource(R.raw.bd);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
           servidor[control] = line;
            while (line != null) {
                control++;
                line = reader.readLine();
                servidor[control] = line;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getServidor(int numero) {
        return this.servidor[numero];
    }
}
