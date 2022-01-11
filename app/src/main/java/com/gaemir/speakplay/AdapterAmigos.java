package com.gaemir.speakplay;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// The adapter class which
// extends RecyclerView Adapter
public class AdapterAmigos extends RecyclerView.Adapter<AdapterAmigos.MyView> {

    // List with String type
    private List<String> list;
    private List<Drawable> imagen;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView extends RecyclerView.ViewHolder {

        // Text View

        TextView textView;
        CircleImageView imagenAmigo;


        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);

            // initialise TextView with id
            textView = (TextView) view.findViewById(R.id.textviewnombre);
            imagenAmigo = (CircleImageView) view.findViewById(R.id.imagenAmigo);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public AdapterAmigos(List<String> horizontalList, List<Drawable> imagen)
    {
        this.list = horizontalList;
        this.imagen = imagen;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType)
    {

        // Inflate item.xml using LayoutInflator
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

        // return itemView
        return new MyView(itemView);
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
        holder.textView.setText(list.get(position));
        holder.imagenAmigo.setImageDrawable(imagen.get(position));
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}