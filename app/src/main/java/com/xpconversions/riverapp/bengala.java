package com.xpconversions.riverapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class bengala extends Activity {

    public static Camera cam = null;// has to be static, otherwise onDestroy() destroys it
    public boolean cameraOn;
    ImageButton menu_begala;
    public boolean buttonPressed;
    public float frequency = 1000;
    SeekBar seekbar;
    ImageButton flashlight_btn;
    Thread t;
    AdView mAdView_bengala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bengala);

        flashlight_btn = findViewById(R.id.flashlight_btn);
        seekbar = findViewById(R.id.seekBar);
        flashlight_btn.setImageResource(R.drawable.flashlight_off);
        mAdView_bengala = findViewById(R.id.adView_bengala);
        menu_begala = findViewById(R.id.menu_bengala);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView_bengala.loadAd(adRequest);


        //Set inmersive mode
        Utility.makeInmmersive(getWindow());


        flashlight_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if(!buttonPressed)
                        {
                                buttonPressed = true;
                                t.start();
                                flashlight_btn.setImageResource(R.drawable.flashlight);
                        } else {
                            buttonPressed = false;
                            flashlight_btn.setImageResource(R.drawable.flashlight_off);
                            t.interrupt();
                            Intent i = new Intent(getBaseContext(), menu.class);
                            startActivity(i);
                        }
            }
        });

            t = new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep((long) frequency);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(frequency == 3){

                                } else {
                                    cameraToggle();
                                }
                                frequency = seekbar.getProgress();
                                Log.v("matilda","this is the freq: " + seekbar.getProgress());
                                switch ((int) frequency){
                                    case 0:
                                        frequency = 2000;
                                        break;
                                    case 1:
                                        frequency = 1000;
                                        break;
                                    case 2:
                                        frequency = 500;
                                        break;
                                    case 3:
                                        frequency = 50000;
                                }
//                                frequency = seekbar.getProgress()* 500 + 500; //Progress goes from 0 to 3, *500 to make it noticable + 500 to have a minimum of half second.

                            }
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        cameraToggle();
                    }

                }
            }
        };


        menu_begala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), menu.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        t.interrupt();
    }

    private void cameraToggle(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = null; // Usually back camera is at 0 position.
            try {
                cameraId = camManager.getCameraIdList()[0];
                if(!cameraOn){
                    camManager.setTorchMode(cameraId, true);   //Turn ON
                    cameraOn = true;
                } else {
                    camManager.setTorchMode(cameraId, false);
                    cameraOn = false;
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
