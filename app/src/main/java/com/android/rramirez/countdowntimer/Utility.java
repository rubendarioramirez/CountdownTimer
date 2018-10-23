package com.android.rramirez.countdowntimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by gusta_000 on 24/8/2016.
 */
public class Utility {

    private static final String PREFS_NAME = BuildConfig.APPLICATION_ID;

    public static final String KEY_COUNT_DOWN_MILLIS = "count_down_millis";
    public static final String KEY_LAST_TIME_MILLIS = "last_time_millis";

    public static String updateCurrentCountDownTime(Context ctx) {
        long defaultValue = 3 * 60 * 1000; //3 minutes
        long countDownMillis = Utility.getLong(ctx, Utility.KEY_COUNT_DOWN_MILLIS, defaultValue);

        long currentTimeMillis = System.currentTimeMillis();
        long lastTimeMillis = Utility.getLong(ctx, Utility.KEY_LAST_TIME_MILLIS, currentTimeMillis);

        long difference = currentTimeMillis - lastTimeMillis;

        //Decrease the counter by the difference in time between this call and the last one
        countDownMillis -= difference;

        String hms;
        if(countDownMillis >= 0) {
            Utility.putLong(ctx, Utility.KEY_COUNT_DOWN_MILLIS, countDownMillis);
            Utility.putLong(ctx, Utility.KEY_LAST_TIME_MILLIS, currentTimeMillis);

            hms = String.format("%02d:%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toDays(countDownMillis),
                    TimeUnit.MILLISECONDS.toHours(countDownMillis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(countDownMillis)),
                    TimeUnit.MILLISECONDS.toMinutes(countDownMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(countDownMillis)),
                    TimeUnit.MILLISECONDS.toSeconds(countDownMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(countDownMillis)));
        }
        else {
            hms = "Completed!";
        }

        return hms;
    }



    public static void putLong(Context ctx, String key, long value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void putString(Context ctx, String key, String value) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static long getLong(Context ctx, String key, long defaultValue) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        long value = settings.getLong(key, defaultValue);
        return value;
    }

    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        String value = settings.getString(key, defaultValue);
        return value;
    }

    //Read from the file. File name is hardcoded inside
    public static String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("offlineData");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    //Check internet status, both WIFI or 3G
    public static boolean isNetworkAvailable(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);

            if (netInfo != null
                    && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null
                        && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }



    public static void makeToast(Context mContext, String texto){
        Toast.makeText(mContext,texto, Toast.LENGTH_SHORT).show();
    }


    public static void saveFile(Context ctx, String currentString){
        String filename = "offlineData";
        FileOutputStream outputStream;

        try {
            outputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(currentString.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public static void makeInmmersive(Window window){
        //Set inmersive mode
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


    }


}
