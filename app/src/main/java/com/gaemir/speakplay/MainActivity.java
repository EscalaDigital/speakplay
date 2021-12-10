package com.gaemir.speakplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    Animation animaBird, animaCorazon, animaSuperman, animaCofre, animaIronman;
    ImageView bird, corazon, superman, cofre, ironman, start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bird = (ImageView) findViewById(R.id.bird);
        corazon = (ImageView) findViewById(R.id.corazon);
        superman = (ImageView) findViewById(R.id.superman);
        cofre = (ImageView) findViewById(R.id.cofre);
        ironman = (ImageView) findViewById(R.id.ironman);
        start = (ImageView) findViewById(R.id.botonstart);


        ironman.setVisibility(View.INVISIBLE);



        animaBird = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bird);
        animaSuperman = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.superman);
        animaCorazon = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.corazon);
        animaCofre = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cofre);
        animaIronman = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ironman);

        bird.startAnimation(animaBird);
        superman.startAnimation(animaSuperman);
        corazon.startAnimation(animaCorazon);
        cofre.startAnimation(animaCofre);


        Intent intent = new Intent(this, MainUser.class);


        this.cofre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == findViewById(R.id.cofre)) {
                    cofre.clearAnimation();
                    cofre.setImageResource(R.drawable.cofreoabierto);
                    int dimensionInPixel = 75;
                    int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());
                    cofre.getLayoutParams().height = dimensionInDp;
                    cofre.getLayoutParams().width = dimensionInDp;

                    ironman.setVisibility(View.VISIBLE);
                    ironman.startAnimation(animaIronman);


                }
            }
        });

        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                startActivity(intent);

            }

        });





    }
}