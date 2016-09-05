package com.android.rramirez.countdowntimer;

import java.text.ParseException;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView dayCount, dayText, horaCount, horaText, minCount, minText, segCount, segText, riverJuega;
    private Context mContext;
    private static final String TAG = "BroadcastTest";
    private Intent intent;
    public static String packageName;
    String url = "http://esteeselfamosoriver.com/app/info.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        packageName = getPackageName();

        //Get the intent from the service
        intent = new Intent(this, TimerService.class);

        //Declare UI elements for MainActivity
        dayCount = (TextView) findViewById(R.id.dayCount);
        dayText = (TextView) findViewById(R.id.diaText);
        horaCount = (TextView) findViewById(R.id.horaCount);
        horaText = (TextView) findViewById(R.id.horaText);
        minCount = (TextView) findViewById(R.id.minCount);
        minText = (TextView) findViewById(R.id.minText);
        segCount = (TextView) findViewById(R.id.segCount);
        segText = (TextView) findViewById(R.id.segText);
        riverJuega = (TextView) findViewById(R.id.riverJuega);
        //Crea la font Custom and assign to the titles
        Typeface myCustomFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Ubuntu-C.ttf");
        Typeface myCustomFontNegrita = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Ubuntu-B.ttf");
        TextView title1 = (TextView) findViewById(R.id.title1);
        title1.setTypeface(myCustomFontNegrita);
        TextView title2 = (TextView) findViewById(R.id.title2);
        title2.setTypeface(myCustomFontNegrita);
        TextView text1 = (TextView) findViewById(R.id.text1);
        text1.setTypeface(myCustomFont);
        TextView text2 = (TextView) findViewById(R.id.text2);
        text2.setTypeface(myCustomFont);

        dayCount.setTypeface(myCustomFontNegrita);
        horaCount.setTypeface(myCustomFontNegrita);
        minCount.setTypeface(myCustomFontNegrita);
        segCount.setTypeface(myCustomFontNegrita);
        dayText.setTypeface(myCustomFontNegrita);
        horaText.setTypeface(myCustomFontNegrita);
        minText.setTypeface(myCustomFontNegrita);
        segText.setTypeface(myCustomFontNegrita);

        //define River
        String fetchTitle1 = Utility.getString(mContext, "title1", "");
        if (fetchTitle1.equals("RIVER")) {
            String text = "<font color=white>RI</font><font color=#cc0029>V</font><font color=white>ER</font>";
            title1.setText(Html.fromHtml(text));
        } else {
            title1.setText(fetchTitle1);
        }

        String fetchTitle2 = Utility.getString(mContext, "title2", "");
        if (fetchTitle2.equals("RIVER")) {
            String text = "<font color=white>RI</font><font color=#cc0029>V</font><font color=white>ER</font>";
            title2.setText(Html.fromHtml(text));
        } else {
            title2.setText(fetchTitle2);
        }

        //Get and Set the titles
        String fetchText1 = Utility.getString(mContext, "text1", "");
        String fetchText2 = Utility.getString(mContext, "text2", "");
        text1.setText(fetchText1);
        text2.setText(fetchText2);
    }

    //Gets the updates for the CountDown.
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                updateGUI(intent); // or whatever method used to update your GUI fields
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };


    private void updateGUI(Intent intent) throws ParseException {
        if (intent.getExtras() != null) {

            String day = Utility.getString(this, "daysRemaining", "");
            String hour = Utility.getString(this, "hoursRemaining", "");
            String min = Utility.getString(this, "minutesRemaining", "");
            String sec = Utility.getString(this, "secRemaining", "");
            dayCount.setText(day);
            horaCount.setText(hour);
            minCount.setText(min);
            segCount.setText(sec);

            int diasFaltan = Integer.parseInt(day);
            int horasFaltan = Integer.parseInt(hour);
            int minFaltan = Integer.parseInt(min);

            if (diasFaltan <= 1) {
                dayText.setText("Día");
            } else {
                dayText.setText("Días");
            }

            if (horasFaltan <= 1) {
                horaText.setText("Hora");
            } else {
                horaText.setText("Horas");
            }

            if (minFaltan <= 1) {
                minText.setText("Minuto");
            } else {
                minText.setText("Minutos");
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(TimerService.COUNTDOWN_BR));
        boolean internet = Utility.isNetworkAvailable(this);
        if (!internet) {
            Utility.makeToast(this, "No se pudo conectar al servidor");
        } else {
            //Start service to fetchData
            new getFechaTask(this.getApplicationContext()).execute(url);
        }

        Log.i(TAG, "Registered broacast receiver");

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
           unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}



