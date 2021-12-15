package com.gaemir.speakplay;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Menu;

import android.view.MenuInflater;
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
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    Adapter adapter;
    LinearLayoutManager HorizontalLayout;
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
        Drawable drawable2 = getResources().getDrawable(R.drawable.person1);
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


        //Reciclerview Horizontal
        recyclerViewHorizontal = (RecyclerView) findViewById(R.id.recyclerview_horizontal);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHorizontal.setLayoutManager(RecyclerViewLayoutManager);

        // Añadir elementos al arraylist
        AddItemsToRecyclerViewArrayList();

        // añadir elementos al adaptador
        adapter = new Adapter(source);

        //cargar el layout de forma horizontal

        HorizontalLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHorizontal.setLayoutManager(HorizontalLayout);

        // añador elementos del adapter
        recyclerViewHorizontal.setAdapter(adapter);
    }

    // Function to add items in RecyclerView.
    public void AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList
        source = new ArrayList<>();
        source.add("gfg");
        source.add("is");
        source.add("best");
        source.add("site");
        source.add("for");
        source.add("interview");
        source.add("preparation");
    }



}

