package com.gaemir.speakplay;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class Vecino implements Parcelable {

    List<String> nombre, juego, user;
    List<String> edad;
    List<String> imagen;


    public Vecino(List<String> nombre, List<String> user, List<String> imagen, List<String> juego, List<String> edad) {
        this.nombre = nombre;
        this.juego = juego;
        this.user = user;
        this.imagen = imagen;
        this.edad = edad;
    }


    protected Vecino(Parcel in) {
        nombre = in.createStringArrayList();
        juego = in.createStringArrayList();
        user = in.createStringArrayList();
        edad = in.createStringArrayList();
        imagen = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(nombre);
        dest.writeStringList(juego);
        dest.writeStringList(user);
        dest.writeStringList(edad);
        dest.writeStringList(imagen);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vecino> CREATOR = new Creator<Vecino>() {
        @Override
        public Vecino createFromParcel(Parcel in) {
            return new Vecino(in);
        }

        @Override
        public Vecino[] newArray(int size) {
            return new Vecino[size];
        }
    };

    public String getNombre(int i) {
        return nombre.get(i);
    }

    public String getJuego(int i) {
        return juego.get(i);
    }

    public String getUser(int i) {
        return user.get(i);
    }

    public String getEdad(int i) {
        return edad.get(i);
    }

    public String getImagen(int i) {
        return imagen.get(i);
    }

    public int getSize(){
        return nombre.size();
    }


}
