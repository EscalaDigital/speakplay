package com.gaemir.speakplay;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// The adapter class which
// extends RecyclerView Adapter
public class AdapterAmigos extends RecyclerView.Adapter<AdapterAmigos.MyView> implements View.OnClickListener {

    // List with String type

    private List<String> list;
    private List<String> user;
    private List<String> nombre;
    private List<Integer> edad;
    private List<Drawable> imagen;
    private List<Integer> tipoAmistad;



    private View.OnClickListener listener;

    public String getUser(int i) {
        return user.get(i);
    }
    public String getNombre(int i) {
        return nombre.get(i);
    }

    public int getTipo(int i) {
        return tipoAmistad.get(i);
    }
    public int getEdad(int i) {
        return edad.get(i);
    }



    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView extends RecyclerView.ViewHolder {

        // Text View

        TextView textView;
        CircleImageView imagenAmigo;


        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view) {
            super(view);

            // initialise TextView with id
            textView = (TextView) view.findViewById(R.id.textviewnombre);
            imagenAmigo = (CircleImageView) view.findViewById(R.id.imagenAmigo);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public AdapterAmigos(List<String> horizontalList, List<String> userAmigo, List<Drawable> imagen, List<Integer> tipoAmistad, List<String> nombre, List<Integer> edad) {
        this.user = userAmigo;
        this.list = horizontalList;
        this.imagen = imagen;
        this.tipoAmistad = tipoAmistad;
        this.nombre = nombre;
        this.edad = edad;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate item.xml using LayoutInflator
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        itemView.setOnClickListener(this);
        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        // Set the text of each item of
        // Recycler view with the list items
        holder.textView.setText(list.get(position));
        holder.imagenAmigo.setImageDrawable(imagen.get(position));


        if (tipoAmistad.get(position) == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#939C9D"));
        }else if(tipoAmistad.get(position) == 1){
            holder.itemView.setBackgroundColor(Color.parseColor("#66D4E8"));
        }
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount() {
        return list.size();
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
}