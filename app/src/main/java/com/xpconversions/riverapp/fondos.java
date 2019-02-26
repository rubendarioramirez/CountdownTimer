package com.xpconversions.riverapp;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class fondos extends Activity implements RewardedVideoAdListener {

    ImageView bg1_iv, bg2_iv, bg3_iv, bg4_iv, bg5_iv, bg6_iv;
    private FirebaseAnalytics mFirebaseAnalytics;
    public int bg1_state, bg2_state,bg3_state,bg4_state,bg5_state,bg6_state;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fondos);

        //Rewarded ad
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getApplicationContext());
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        bg1_iv = findViewById(R.id.bg1_iv);
        bg2_iv = findViewById(R.id.bg2_iv);
        bg3_iv = findViewById(R.id.bg3_iv);
        bg4_iv = findViewById(R.id.bg4_iv);
        bg5_iv = findViewById(R.id.bg5_iv);
        bg6_iv = findViewById(R.id.bg6_iv);

        //Get the state of each background
        SharedPreferences prefs = getSharedPreferences("bg_state", MODE_PRIVATE);
        bg1_state = prefs.getInt("bg1", 0);
        bg2_state = prefs.getInt("bg2", 0);
        bg3_state = prefs.getInt("bg3", 0);
        bg4_state = prefs.getInt("bg4", 0);
        bg5_state = prefs.getInt("bg5", 0);
        bg6_state = prefs.getInt("bg6", 0);
        updateUI();

        bg1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               background_clicked(0,"bg1");

            }
        });
        bg2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background_clicked(1,"bg2");
            }
        });

        bg3_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background_clicked(2,"bg3");
            }
        });

        bg4_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background_clicked(3,"bg4");
            }
        });

        bg5_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background_clicked(4,"bg5");
            }
        });

        bg6_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background_clicked(5,"bg6");
            }
        });



    }

    public void background_clicked(int bg, String bg_index){
        switch (bg){
            case 0:
                if(bg1_state == 0){
                    showAd(bg, bg_index);
                } else {
                    int bg_name = bg + 1;
                    saveToPref(bg_name);
                }
                break;
            case 1:
                if(bg2_state == 0){
                    showAd(bg, bg_index);
                } else {
                    int bg_name = bg + 1;
                    saveToPref(bg_name);
                }
                break;
            case 2:
                if(bg3_state == 0){
                    showAd(bg, bg_index);
                } else {
                    int bg_name = bg + 1;
                    saveToPref(bg_name);
                }
                break;
            case 3:
                if(bg4_state == 0){
                    showAd(bg, bg_index);
                } else {
                    int bg_name = bg + 1;
                    saveToPref(bg_name);
                }
                break;
            case 4:
                if(bg5_state == 0){
                    showAd(bg, bg_index);
                } else {
                    int bg_name = bg + 1;
                    saveToPref(bg_name);
                }
                break;
            case 5:
                if(bg5_state == 0){
                    showAd(bg, bg_index);
                } else {
                    int bg_name = bg + 1;
                    saveToPref(bg_name);
                }
                break;
            }

        }

    public void saveToPref(int bg){
            SharedPreferences.Editor editor = getSharedPreferences("my_sharepref", MODE_PRIVATE).edit();
            editor.putInt("bgImage", bg);
            editor.apply();
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
    }

    public void saveState(String bg_index){
        SharedPreferences.Editor editor = getSharedPreferences("bg_state", MODE_PRIVATE).edit();
        editor.putInt(bg_index, 1);
        editor.apply();
    }

    public void showAd(int bg, String bg_index){
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
            switch (bg){
                case 0:
                    bg1_state = 1;
                    break;
                case 1:
                    bg2_state = 1;
                    break;
                case 2:
                    bg3_state = 1;
                    break;
                case 3:
                    bg4_state = 1;
                    break;
                case 4:
                    bg5_state = 1;
                    break;
                case 5:
                    bg6_state = 1;
                    break;

            }
            saveState(bg_index);
        }
    }


    public void updateUI(){
        if(bg1_state == 0){
            bg1_iv.setImageResource(R.drawable.bg1_grey);
        } else {
            bg1_iv.setImageResource(R.drawable.bg1);
        }
        if(bg2_state == 0){
            bg2_iv.setImageResource(R.drawable.bg2_grey);
        } else {
            bg2_iv.setImageResource(R.drawable.bg2);
        }
        if(bg3_state == 0){
            bg3_iv.setImageResource(R.drawable.bg3_grey);
        } else {
            bg3_iv.setImageResource(R.drawable.bg3);
        }
        if(bg4_state == 0){
            bg4_iv.setImageResource(R.drawable.bg4_grey);
        } else {
            bg4_iv.setImageResource(R.drawable.bg4);
        }
        if(bg5_state == 0){
            bg5_iv.setImageResource(R.drawable.bg5_grey);
        } else {
            bg5_iv.setImageResource(R.drawable.bg5);
        }
        if(bg6_state == 0){
            bg6_iv.setImageResource(R.drawable.bg6_grey);
        } else {
            bg6_iv.setImageResource(R.drawable.bg6);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.reward_ad_unit_id),
                new AdRequest.Builder()
                        .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                        .build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
