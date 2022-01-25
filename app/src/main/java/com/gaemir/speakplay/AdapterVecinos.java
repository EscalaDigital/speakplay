package com.gaemir.speakplay;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// The adapter class which
// extends RecyclerView Adapter
public class AdapterVecinos extends RecyclerView.Adapter<AdapterVecinos.MyView> implements View.OnClickListener , Serializable {

    // List with String type

    private List<String> nombre;
    private List<Drawable> imagen;
    private List<String> juego;
    private List<String> user;

    public String getNombre(int i) {
        return nombre.get(i);
    }

    public String getUser(int i) {
        return user.get(i);
    }

    private View.OnClickListener listener;


    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView extends RecyclerView.ViewHolder {

        // Text View

        TextView textView, juego;
        CircleImageView imagenAmigo;


        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view) {
            super(view);

            // initialise TextView with id
            textView = (TextView) view.findViewById(R.id.textnombre);
            juego = (TextView) view.findViewById(R.id.textViewJuego);
            imagenAmigo = (CircleImageView) view.findViewById(R.id.imagenVecino);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public AdapterVecinos(List<String> horizontalList, List<Drawable> imagen, List<String> juego,List<String> user) {

        this.user = user;
        this.nombre = horizontalList;
        this.imagen = imagen;
        this.juego = juego;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate item.xml using LayoutInflator
        View cercanosView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cercanos_view, parent, false);

        cercanosView.setOnClickListener(this);


        // return cercanosView
        return new MyView(cercanosView);


    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        // Set the text of each item of
        // Recycler view with the list items
        holder.textView.setText(nombre.get(position));
        holder.juego.setText(juego.get(position));
        holder.imagenAmigo.setImageDrawable(imagen.get(position));

    }

    // Vaciar el adapter
    @Override
    public int getItemCount() {
        return nombre.size();
    }


    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public void clear() {
        int size = nombre.size();
        System.out.println(size);
        System.out.println(nombre.get(4));
        System.out.println(juego.get(4));
        System.out.println(imagen.get(4));
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                System.out.println("vuelta" + i);
                try {
                    nombre.remove(i);
                    juego.remove(i);
                    imagen.remove(i);
                    notifyItemRemoved(i);
                } catch (Exception e) {
                    System.out.println("Problema al intentar borrar un elemento");
                }

            }


        }
    }
}