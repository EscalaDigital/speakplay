package com.gaemir.speakplay;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Clase vecino
 * Clase que nos permite recopilar los datos de los vecinos obtenidos en la busqueda.
 * El objetivo de esta clase es poder navegar entre vecinos sin tener que hacer consultas constantes al servidor.
 * Se implementa Parcelable para poder insertarlo en un intent y pasarlo entre activities
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */


public class Vecino implements Parcelable {

    List<String> nombre, juego, user;
    List<String> edad;
    List<String> imagen;


    //Contructor de vecion
    public Vecino(List<String> nombre, List<String> user, List<String> imagen, List<String> juego, List<String> edad) {
        this.nombre = nombre;
        this.juego = juego;
        this.user = user;
        this.imagen = imagen;
        this.edad = edad;
    }


    //Contructor necesario para que la clase sea parcelable
    protected Vecino(Parcel in) {
        nombre = in.createStringArrayList();
        juego = in.createStringArrayList();
        user = in.createStringArrayList();
        edad = in.createStringArrayList();
        imagen = in.createStringArrayList();
    }

    /**
     * Pasamos los valores al Parcel
     *
     * @param dest  Parcel
     * @param flags key
     */
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

    /**
     * Contructor de Vecino a partir de parcel
     */
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


    /**
     * Getter obtenemos nombre de vecino
     *
     * @param i key
     * @return nombre vecino String
     */
    public String getNombre(int i) {
        return nombre.get(i);
    }

    /**
     * Getter obtenemos juego de vecino
     *
     * @param i key
     * @return juego vecino String
     */
    public String getJuego(int i) {
        return juego.get(i);
    }

    /**
     * Getter obtenemos user de vecino
     *
     * @param i key
     * @return user vecino String
     */
    public String getUser(int i) {
        return user.get(i);
    }

    /**
     * Getter obtenemos edad de vecino
     *
     * @param i key
     * @return edad vecino String
     */
    public String getEdad(int i) {
        return edad.get(i);
    }

    /**
     * Getter obtenemos imagen de vecino
     *
     * @param i key
     * @return imagen vecino String
     */
    public String getImagen(int i) {
        return imagen.get(i);
    }

    /**
     * Getter obtenemos numero de vecinos
     *
     * @return tamaño vecino Int
     */
    public int getSize() {
        return nombre.size();
    }


}
