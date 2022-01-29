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


/**
 * Adapter de vecinos (personas que cumplen los requisitos de búsqueda establecidos en preferences)
 * Este adapter recopila los datos referentes a vecinos cercanos al usuario
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */
public class AdapterVecinos extends RecyclerView.Adapter<AdapterVecinos.MyView> implements View.OnClickListener, Serializable {

    // Elementros para recopilar datos en el adaptador

    private List<String> nombre;
    private List<Drawable> imagen;
    private List<String> juego;
    private List<String> user;


    //getters necesarios para obtener datos del adapter

    /**
     * Getter que obtiene el usuario seleccionado
     *
     * @param i posicion en el List
     * @return
     */
    public String getUser(int i) {
        return user.get(i);
    }

    private View.OnClickListener listener;

    /**
     * Clase que extiende de reciclerview para poder cargar sus funciones y vincular los datos a este
     */
    public class MyView extends RecyclerView.ViewHolder {

        //Elementos del recyclerview

        TextView textView, juego;
        CircleImageView imagenAmigo;


        /**
         * Constructor de la vista
         *
         * @param view elementos layout prediseñado
         */
        public MyView(View view) {
            super(view);

            // initialise TextView with id
            textView = (TextView) view.findViewById(R.id.textnombre);
            juego = (TextView) view.findViewById(R.id.textViewJuego);
            imagenAmigo = (CircleImageView) view.findViewById(R.id.imagenVecino);
        }
    }

    /**
     * Constructor de la clase. Obtenemos los diferentes valores de vecinos mediante listas de valores
     *
     * @param horizontalList Nombre del usuario
     * @param imagen         Imagen del Usuario
     * @param juego          Juego del Usuario
     * @param user           Usuario del Usuario
     */
    public AdapterVecinos(List<String> horizontalList, List<Drawable> imagen, List<String> juego, List<String> user) {

        this.user = user;
        this.nombre = horizontalList;
        this.imagen = imagen;
        this.juego = juego;
    }

    /**
     * Clases que pasa los elementos a la vista
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {


        View cercanosView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cercanos_view, parent, false);

        cercanosView.setOnClickListener(this);

        return new MyView(cercanosView);


    }

    /**
     * Clase que carga los elementos en el reciclerview (cambiamos de color el fondo de cada Amigo si la amistad esta confirmada o no)
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyView holder, final int position) {


        holder.textView.setText(nombre.get(position));
        holder.juego.setText(juego.get(position));
        holder.imagenAmigo.setImageDrawable(imagen.get(position));

    }

    /**
     * getter que obtiene el tamaño de la lista de usuarios
     *
     * @return numero de usuarios
     */
    @Override
    public int getItemCount() {
        return nombre.size();
    }

    /**
     * Listener que se mantiene a la espera de un clic sobre un amigo
     *
     * @param listener listener
     */
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * Método que atrapa la acccion de onclick y llama al listener
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }


}