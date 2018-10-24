package com.android.rramirez.countdowntimer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.security.Policy;

public class bengala extends Activity {

    public static Camera cam = null;// has to be static, otherwise onDestroy() destroys it
    public boolean cameraOn;
    public boolean buttonPressed;
    public boolean threadInterrupted;
    public float frequency = 1000;
    SeekBar seekbar;
    ImageButton flashlight_btn;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bengala);

        flashlight_btn = findViewById(R.id.flashlight_btn);
        seekbar = findViewById(R.id.seekBar);


        //Set inmersive mode
        Utility.makeInmmersive(getWindow());


        flashlight_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if(!buttonPressed){
                            if(!threadInterrupted){
                                buttonPressed = true;
                                t.start();
                                }

                        } else {
                            buttonPressed = false;
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
                                cameraToggle();
                                frequency = seekbar.getProgress()* 500 + 500; //Progress goes from 0 to 3, *500 to make it noticable + 500 to have a minimum of half second.
                            }
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        cameraToggle();
                    }

                }
            }
        };


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
