package com.android.rramirez.countdowntimer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class menu extends Activity {

    TextView proximo, bengala, alenta, fondos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        //Set inmersive mode
        Utility.makeInmmersive(getWindow());


        proximo = findViewById(R.id.proximo);
        bengala = findViewById(R.id.bengala);
        alenta = findViewById(R.id.alenta);
        fondos = findViewById(R.id.fondos);



        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }
        });

        bengala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), bengala.class);
                startActivity(i);
            }
        });

        alenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Add Alenta activity
            }
        });


        fondos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Add Fondos activity
            }
        });

    }
}
