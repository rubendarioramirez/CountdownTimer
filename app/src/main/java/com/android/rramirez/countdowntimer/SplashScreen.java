package com.android.rramirez.countdowntimer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

import static com.android.rramirez.countdowntimer.Utility.isNetworkAvailable;
import static com.android.rramirez.countdowntimer.Utility.updateCurrentCountDownTime;

/**
 * Created by rramirez on 8/16/16.
 */
public class SplashScreen extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "OWVThf7yhua8aMKdFQzAacjke";
    private static final String TWITTER_SECRET = "OZFra5DqU8zeCycKuJfvuakuxBg1wQczeh0EPwj84WqaKAmD84";


    //JSON URL
    String url = "http://esteeselfamosoriver.com/app/info.php";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.splash);
        mContext = this.getApplicationContext();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(4000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();

        //Start service to fetchData
        new getFechaTask(this.getApplicationContext()).execute(url);
        startService(new Intent(this, TimerService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


}