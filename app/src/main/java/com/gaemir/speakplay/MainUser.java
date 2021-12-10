package com.gaemir.speakplay;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainUser extends AppCompatActivity {

    private Toolbar toolbar;

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


        toolbar.setSubtitle("Test Subtitle");
        toolbar.inflateMenu(R.menu.menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.menufiltros)
                {
                    new AlertDialog.Builder(MainUser.this)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else if(item.getItemId()== R.id.menumapa)
                {
                    // do something
                }
                else{
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

        HorizontalLayout = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
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