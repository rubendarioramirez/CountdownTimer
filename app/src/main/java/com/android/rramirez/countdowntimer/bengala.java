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
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.security.Policy;

public class bengala extends Activity {

    public static Camera cam = null;// has to be static, otherwise onDestroy() destroys it
    public boolean cameraOn;
    public boolean buttonPressed;
    ImageButton flashlight_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bengala);

        flashlight_btn = findViewById(R.id.flashlight_btn);

        //Set inmersive mode
        Utility.makeInmmersive(getWindow());


        flashlight_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        while (!isInterrupted()) {
                            try {
                                Thread.sleep(1000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        cameraToggle();
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                };
                t.start();
            }
        });




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
