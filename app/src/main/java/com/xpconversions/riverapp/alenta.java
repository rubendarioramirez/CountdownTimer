package com.xpconversions.riverapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class alenta extends Activity {

    TextView alenta_tv;
    ImageView alenta_iv;
    Thread t;
    List<String> alenta_array;
    ImageView menu_alenta;
    int textIndex = 0;
    public boolean backgroundRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alenta);
        Utility.makeInmmersive(this.getWindow());
        Intent intent = getIntent();
        String cancion = intent.getStringExtra("cancion");

        alenta_array = new ArrayList<String>();

        menu_alenta = findViewById(R.id.menu_alenta);
        alenta_tv = findViewById(R.id.alenta_tv);
        alenta_iv = findViewById(R.id.alenta_iv);



        //Crea la font Custom and assign to the titles
        Typeface myCustomFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Ubuntu-C.ttf");
        Typeface myCustomFontNegrita = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Ubuntu-B.ttf");
        TextView title1 = findViewById(R.id.alenta_tv);
        title1.setTypeface(myCustomFontNegrita);

        switch (cancion){
            case "alenta":
                alenta_array.add("ESTE");
                alenta_array.add("ESTE");
                alenta_array.add("ES EL");
                alenta_array.add("ES EL");
                alenta_array.add("FAMOSO");
                alenta_array.add("FAMOSO");
                alenta_array.add("RIVER");
                alenta_array.add("RIVER");
                break;
            case "mune":
                alenta_array.add("MUÑEEEEE");
                alenta_array.add("MUÑEEEEE");
                break;
            case "gol":
                alenta_array.add("GOOOOOOOOL!");
                alenta_array.add("GOOOOOOOOL!");
                break;
            case "amigo":
                alenta_array.add("RIVER");
                alenta_array.add("RIVER");
                alenta_array.add("MI BUEN AMIGO");
                alenta_array.add("MI BUEN AMIGO");
                alenta_array.add("ESTA CAMPAÑA");
                alenta_array.add("ESTA CAMPAÑA");
                alenta_array.add("VOLVEREMO");
                alenta_array.add("VOLVEREMO");
                alenta_array.add("A ESTAR");
                alenta_array.add("A ESTAR");
                alenta_array.add("CONTIGO");
                alenta_array.add("CONTIGO");
                break;
        }





        t = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(400);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(textIndex>=alenta_array.size()){
                                    textIndex = 0;
                                } else {
                                    alenta_tv.setText(alenta_array.get(textIndex));
                                    setBackgroundColor();
                                    textIndex++;
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();

                    }

                }
            }
        };

        t.start();


        menu_alenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), menu.class);
                startActivity(i);
            }
        });
    }


    private void setBackgroundColor(){
        if(!backgroundRed){
            alenta_iv.setImageResource(R.color.river_red);
            backgroundRed=!backgroundRed;
        } else {
            alenta_iv.setImageResource(R.color.river_white);
            backgroundRed=!backgroundRed;
        }

    }


}
