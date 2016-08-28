package com.android.rramirez.countdowntimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView dayCount, dayText, horaCount, horaText, minCount, minText, segCount, segText, riverJuega;
    private Context mContext;
    String url = "http://esteeselfamosoriver.com/app/info.php";
    private static final String TAG = "BroadcastTest";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint);
        mContext = getApplicationContext();

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
       Typeface myCustomFont = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Ubuntu-C.ttf");
       Typeface myCustomFontNegrita = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Ubuntu-B.ttf");
        TextView title1 = (TextView) findViewById(R.id.title1);
        title1.setTypeface(myCustomFontNegrita);
        TextView title2 = (TextView) findViewById(R.id.title2);
        title2.setTypeface(myCustomFontNegrita);
        TextView text1 = (TextView) findViewById(R.id.text1);
        text1.setTypeface(myCustomFont);
        TextView text2 = (TextView) findViewById(R.id.text2);
        text2.setTypeface(myCustomFont);


            //Get and Set the titles
            String fetchTitle1 = Utility.getString(mContext,"title1","");
            String fetchTitle2 = Utility.getString(mContext,"title2","");
            String fetchText1 = Utility.getString(mContext,"text1","");
            String fetchText2 = Utility.getString(mContext,"text2","");
            title1.setText(fetchTitle1);
            title2.setText(fetchTitle2);
            text1.setText(fetchText1);
            text2.setText(fetchText2);

            //Start the service
            startService(new Intent(this, TimerService.class));
            intent = new Intent(this, TimerService.class);
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

            //Gets the missing time in LONG format
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            //Convert the LONG to a date
            String fecha = getDate(millisUntilFinished, "dd/MM/yyyy hh:mm:ss");

            //Parse the date into days,hours,minutes and seconds
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            try {
                Date futureDate = format.parse(fecha);
                Date currentDate = new Date();
                if (!currentDate.after(futureDate)) {
                    long diff = futureDate.getTime()
                            - currentDate.getTime();
                    long days = diff / (24 * 60 * 60 * 1000);
                    diff -= days * (24 * 60 * 60 * 1000);
                    long hours = diff / (60 * 60 * 1000);
                    diff -= hours * (60 * 60 * 1000);
                    long minutes = diff / (60 * 1000);
                    diff -= minutes * (60 * 1000);
                    long seconds = diff / 1000;
                    dayCount.setText("" + String.format("%02d", days));
                    horaCount.setText("" + String.format("%02d", hours));
                    minCount.setText("" + String.format("%02d", minutes));
                    segCount.setText("" + String.format("%02d", seconds));
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


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

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(TimerService.COUNTDOWN_BR));
        //startService(intent);
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
        stopService(new Intent(this, TimerService.class));
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }




    //deprecated Countdown on mainactivity
    //////////////////COUNT DOWN START/////////////////////////
//    public void countDownStart(String eventDate) {
//
//        final String fetchFecha = eventDate;
//
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                handler.postDelayed(this, 1000);
//                try {
//                    SimpleDateFormat dateFormat = new SimpleDateFormat(
//                            "yyyy-MM-dd HH:mm:ss");
//                    // Here Set your Event Date
//                    Date futureDate = dateFormat.parse(fetchFecha);
//                    Date currentDate = new Date();
//                    if (!currentDate.after(futureDate)) {
//                        long diff = futureDate.getTime()
//                                - currentDate.getTime();
//                        long days = diff / (24 * 60 * 60 * 1000);
//                        diff -= days * (24 * 60 * 60 * 1000);
//                        long hours = diff / (60 * 60 * 1000);
//                        diff -= hours * (60 * 60 * 1000);
//                        long minutes = diff/(60*1000);
//                        diff -= minutes * (60 * 1000);
//                        long seconds = diff / 1000;
//                        dayCount.setText("" + String.format("%02d", days));
//                        horaCount.setText("" + String.format("%02d", hours));
//                        minCount.setText("" + String.format("%02d", minutes));
//                        segCount.setText("" + String.format("%02d", seconds));
//
//                        //Set widget counter Time
//                        String startDays = dayCount.getText().toString();
//                        String startHours = horaCount.getText().toString();
//                        String startMinutes = minCount.getText().toString();
//                        String startSeconds = segCount.getText().toString();
//
//                        String updateFecha = startDays + ":" + startHours + ":" + startMinutes + ":" + startMinutes;
//                        Utility.putString(getApplicationContext(),"fecha", updateFecha);
//
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        };
//        handler.postDelayed(runnable, 0);
//
//    }

    private void createNotification(String title, String content){

        //Get the sound and convert to URI
        Uri soundCancha = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hoy_river_app_ringtone);


        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.big_notificationicon)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setSound(soundCancha);
        // Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
                mNotificationManager.notify(0, mBuilder.build());


    }

}



