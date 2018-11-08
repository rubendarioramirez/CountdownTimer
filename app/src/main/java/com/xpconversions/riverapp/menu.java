package com.xpconversions.riverapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class menu extends Activity {

    TextView proximo, bengala, alenta, fondos;
    AdView mAdView_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        //Set inmersive mode
        Utility.makeInmmersive(getWindow());

        cameraToggle();

        proximo = findViewById(R.id.proximo);
        bengala = findViewById(R.id.bengala);
        alenta = findViewById(R.id.alenta);
        fondos = findViewById(R.id.fondos);

        mAdView_menu = findViewById(R.id.adView_menu);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView_menu.loadAd(adRequest);


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
                Intent i = new Intent(getBaseContext(), alenta_list.class);
                startActivity(i);
            }
        });


        fondos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), fondos.class);
                startActivity(i);
            }
        });

    }

    private void cameraToggle(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = null; // Usually back camera is at 0 position.
            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.makeInmmersive(getWindow());
    }
}
