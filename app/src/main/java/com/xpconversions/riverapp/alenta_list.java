package com.xpconversions.riverapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class alenta_list extends Activity {

    TextView river,mune,gol,famoso;
    AdView mAdView_alentalist;
    ImageView menu_alenta_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alenta_list);

        Utility.makeInmmersive(this.getWindow());

        river = findViewById(R.id.river_amigo);
        mune = findViewById(R.id.mune);
        gol = findViewById(R.id.gol);
        famoso = findViewById(R.id.famoso_river);
        menu_alenta_list = findViewById(R.id.menu_alenta_list);
        mAdView_alentalist = findViewById(R.id.adView_alentalist);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView_alentalist.loadAd(adRequest);

        river.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), alenta.class);
                i.putExtra("cancion", "amigo");
                startActivity(i);
            }
        });

        mune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), alenta.class);
                i.putExtra("cancion", "mune");
                startActivity(i);
            }
        });

        gol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), alenta.class);
                i.putExtra("cancion", "gol");
                startActivity(i);
            }
        });


        famoso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), alenta.class);
                i.putExtra("cancion", "alenta");
                startActivity(i);
            }
        });

        menu_alenta_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), menu.class);
                startActivity(i);
            }
        });
    }
    }

