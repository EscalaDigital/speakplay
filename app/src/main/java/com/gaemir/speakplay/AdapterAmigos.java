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

/**
 * Adapter de amigos (personas que han solicitado amistad o que se han cofirmado como amigos)
 * Este adapter recopila los datos referentes a "amigos"
 *
 * @author Gabriel Orozco Frutos
 * @version 0.1, 2022/29/01
 */
public class AdapterAmigos extends RecyclerView.Adapter<AdapterAmigos.MyView> implements View.OnClickListener {

    // Elementros para recopilar datos en el adaptador

    private List<String> list;
    private List<String> user;
    private List<String> nombre;
    private List<Integer> edad;
    private List<Drawable> imagen;
    private List<Integer> tipoAmistad;


    private View.OnClickListener listener;

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

    /**
     * Getter que obtiene el nombre de usuario seleccionado
     *
     * @param i posicion en el List
     * @return
     */
    public String getNombre(int i) {
        return nombre.get(i);
    }

    /**
     * Getter que obtiene el tipo de usuario seleccionado
     *
     * @param i posicion en el List
     * @return
     */

    public int getTipo(int i) {
        return tipoAmistad.get(i);
    }

    /**
     * Getter que obtiene ella edad del usuario seleccionado
     *
     * @param i posicion en el List
     * @return
     */
    public int getEdad(int i) {
        return edad.get(i);
    }

    /**
     * Clase que extiende de reciclerview para poder cargar sus funciones y vincular los datos a este
     */


    public class MyView extends RecyclerView.ViewHolder {

        //Elementos del recyclerview

        TextView textView;
        CircleImageView imagenAmigo;


        /**
         * Constructor de la vista
         *
         * @param view elementos layout prediseñado
         */
        public MyView(View view) {
            super(view);

            // initialise TextView with id
            textView = (TextView) view.findViewById(R.id.textviewnombre);
            imagenAmigo = (CircleImageView) view.findViewById(R.id.imagenAmigo);
        }
    }

    /***
     * Constructor de la clase. Obtenemos los diferentes valores de Amigos mediante listas de valores
     * @param horizontalList NickDiscord del amigo
     * @param userAmigo usuario del Amigo
     * @param imagen imagen del amigo
     * @param tipoAmistad tipo de relacion
     * @param nombre nombre del amigo
     * @param edad edad del amigo
     */
    public AdapterAmigos(List<String> horizontalList, List<String> userAmigo, List<Drawable> imagen, List<Integer> tipoAmistad, List<String> nombre, List<Integer> edad) {
        this.user = userAmigo;
        this.list = horizontalList;
        this.imagen = imagen;
        this.tipoAmistad = tipoAmistad;
        this.nombre = nombre;
        this.edad = edad;
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

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        itemView.setOnClickListener(this);

        return new MyView(itemView);
    }

    /**
     * Clase que carga los elementos en el reciclerview (cambiamos de color el fondo de cada Amigo si la amistad esta confirmada o no)
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyView holder, final int position) {


        holder.textView.setText(list.get(position));
        holder.imagenAmigo.setImageDrawable(imagen.get(position));

        if (tipoAmistad.get(position) == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#939C9D"));
        } else if (tipoAmistad.get(position) == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#66D4E8"));
        }
    }

    /**
     * getter que obtiene el tamaño de la lista de amigos
     *
     * @return numero de amigos
     */
    @Override
    public int getItemCount() {
        return list.size();
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