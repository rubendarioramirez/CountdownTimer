package com.android.rramirez.countdowntimer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static com.android.rramirez.countdowntimer.Utility.isNetworkAvailable;
import static com.android.rramirez.countdowntimer.Utility.updateCurrentCountDownTime;

/**
 * Created by rramirez on 8/16/16.
 */
public class SplashScreen extends Activity {

    //JSON URL
    String url = "http://esteeselfamosoriver.com/app/info.php";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mContext = this.getApplicationContext();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();

            if(Utility.isNetworkAvailable(mContext)){
                new getFechaTask(this.getApplicationContext()).execute(url);
                Utility.makeToast(mContext,"Informacion actualizada");
            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}