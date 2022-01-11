package com.gaemir.speakplay;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainUser extends AppCompatActivity {

    private Toolbar toolbar;
    CircleImageView logoPerfil;

    //Elementos para el recyclerview horizontal
    RecyclerView recyclerViewHorizontal;
    ArrayList<String> source;
    ArrayList<Drawable> imagenesAmigos;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    AdapterAmigos adapter;
    LinearLayoutManager HorizontalLayout;



    //Elementos para el recyclerview vertical
    RecyclerView recyclerViewVertical;
    ArrayList<String> nombreVertical, juegosVecinos;
    ArrayList<Drawable> imagenesVecinos;
    RecyclerView.LayoutManager RecyclerViewLayoutManagerVertical;
    AdapterVecinos adapterVecinos;
    LinearLayoutManager verticalLayout;





    View ChildView;
    int RecyclerViewItemPosition;

    //Elementos para el recyclerview Vertical


    //todo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_user);

        toolbar = findViewById(R.id.toolbarPrincipal);

        //tomamos la imagen y la insertamos en la toolbar

        logoPerfil = findViewById(R.id.imagenPerfil);
        Drawable drawable2 = getResources().getDrawable(R.drawable.a1);
        logoPerfil.setImageDrawable(drawable2);
        toolbar.inflateMenu(R.menu.menu);






        this.logoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainUser.this, PerfilActivity.class));
            }
        });




        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.menufiltros) {
                    startActivity(new Intent(MainUser.this, FiltrosActivity.class));
                } else if (item.getItemId() == R.id.menumapa) {
                    startActivity(new Intent(MainUser.this, MapaActivity.class));


                } else {
                    // do something
                }

                return false;
            }
        });



        // Añadir elementos al arraylist
        AddItemsToRecyclerViewArrayList();

        //Reciclerview Horizontal//
        //////////////////////////
        recyclerViewHorizontal = (RecyclerView) findViewById(R.id.recyclerview_horizontal);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHorizontal.setLayoutManager(RecyclerViewLayoutManager);



        // añadir elementos al adaptador
        adapter = new AdapterAmigos(source, imagenesAmigos);

        //cargar el layout de forma horizontal

        HorizontalLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHorizontal.setLayoutManager(HorizontalLayout);

        // añador elementos del adapter
        recyclerViewHorizontal.setAdapter(adapter);


        //Reciclerview vertical//
        //////////////////////////
        recyclerViewVertical = (RecyclerView) findViewById(R.id.recyclerview_vertical);
        RecyclerViewLayoutManagerVertical = new LinearLayoutManager(getApplicationContext());
        recyclerViewVertical.setLayoutManager(RecyclerViewLayoutManagerVertical);

        // añadir elementos al adaptador
        adapterVecinos = new AdapterVecinos(nombreVertical, imagenesVecinos, juegosVecinos);

        //cargar el layout de forma horizontal

        verticalLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewVertical.setLayoutManager(verticalLayout);

        // añador elementos del adapter
        recyclerViewVertical.setAdapter(adapterVecinos);
    }

    // Function to add items in RecyclerView.
    public void AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList
        source = new ArrayList<>();
        source.add("RedFire");
        source.add("Starman");
        source.add("Gaemir");
        source.add("Holilad");
        source.add("Dangerman");
        source.add("Lolipower");
        source.add("Goku");

        imagenesAmigos = new ArrayList<>();
        imagenesAmigos.add(getDrawable(R.drawable.a2));
        imagenesAmigos.add(getDrawable(R.drawable.a3));
        imagenesAmigos.add(getDrawable(R.drawable.a4));
        imagenesAmigos.add(getDrawable(R.drawable.a5));
        imagenesAmigos.add(getDrawable(R.drawable.a6));
        imagenesAmigos.add(getDrawable(R.drawable.a7));
        imagenesAmigos.add(getDrawable(R.drawable.a8));

        nombreVertical = new ArrayList<>();
        nombreVertical.add("RedFire");
        nombreVertical.add("Starman");
        nombreVertical.add("Gaemir");
        nombreVertical.add("Holilad");
        nombreVertical.add("Dangerman");
        nombreVertical.add("Lolipower");
        nombreVertical.add("Goku");

        imagenesVecinos = new ArrayList<>();
        imagenesVecinos.add(getDrawable(R.drawable.a2));
        imagenesVecinos.add(getDrawable(R.drawable.a3));
        imagenesVecinos.add(getDrawable(R.drawable.a4));
        imagenesVecinos.add(getDrawable(R.drawable.a5));
        imagenesVecinos.add(getDrawable(R.drawable.a6));
        imagenesVecinos.add(getDrawable(R.drawable.a7));
        imagenesVecinos.add(getDrawable(R.drawable.a8));

        juegosVecinos = new ArrayList<>();
        juegosVecinos.add("RedFire");
        juegosVecinos.add("Starman");
        juegosVecinos.add("Gaemir");
        juegosVecinos.add("Holilad");
        juegosVecinos.add("Dangerman");
        juegosVecinos.add("Lolipower");
        juegosVecinos.add("Goku");








    }



}

