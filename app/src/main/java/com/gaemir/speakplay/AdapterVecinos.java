package com.gaemir.speakplay;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// The adapter class which
// extends RecyclerView Adapter
public class AdapterVecinos extends RecyclerView.Adapter<AdapterVecinos.MyView> {

    // List with String type
    private List<String> nombre;
    private List<Drawable> imagen;
    private List<String> juego;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView extends RecyclerView.ViewHolder {

        // Text View

        TextView textView, juego;
        CircleImageView imagenAmigo;


        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);

            // initialise TextView with id
            textView = (TextView) view.findViewById(R.id.textnombre);
            juego = (TextView) view.findViewById(R.id.textViewJuego);
            imagenAmigo = (CircleImageView) view.findViewById(R.id.imagenVecino);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public AdapterVecinos(List<String> horizontalList, List<Drawable> imagen, List<String> juego)
    {
        this.nombre = horizontalList;
        this.imagen = imagen;
        this.juego = juego;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType)
    {

        // Inflate item.xml using LayoutInflator
        View cercanosView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cercanos_view,parent,false);

        // return cercanosView
        return new MyView(cercanosView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,final int position)
    {

        // Set the text of each item of
        // Recycler view with the list items
        holder.textView.setText(nombre.get(position));
        holder.juego.setText(juego.get(position));
        holder.imagenAmigo.setImageDrawable(imagen.get(position));

    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return nombre.size();
    }
}