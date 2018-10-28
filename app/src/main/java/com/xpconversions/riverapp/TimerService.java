package com.xpconversions.riverapp;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimerService extends Service {

    private final static String TAG = "BroadcastService";
    Runnable runnable;
    Handler handler;

    public boolean hourAlarm = false;
    public boolean minuteAlarm = false;
    public boolean finalAlarm = false;

    public static final String COUNTDOWN_BR = "your_package_name.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);
    public static Context context;
    String url = "http://esteeselfamosoriver.com/app/info.php";

    public boolean isPlaying;

    @Override
    public void onCreate() {
        //Context
        context = this;


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    //Fetch fecha from SharedPref
                    String fetchFecha = Utility.getString(context, "fecha", "");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // Here Set your Event Date
                    Date futureDate = dateFormat.parse(fetchFecha);
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

                        bi.putExtra("countdown", diff);
                        Utility.putString(context, "daysRemaining", String.format("%d", days));
                        Utility.putString(context, "hoursRemaining", String.format("%d", hours));
                        Utility.putString(context, "minutesRemaining", String.format("%d", minutes));
                        Utility.putString(context, "secRemaining", String.format("%d", seconds));
                        Utility.putString(context, "jugando", "false");

                        //Get Titles
                        String titleWidget1 = Utility.getString(context, "title1", "");
                        String titleWidget2 = Utility.getString(context, "title2", "");
                        String titleWidget3 = Utility.getString(context, "text1", "");


                        //Parse the data to fit into String no decimal format
                        String countDays = String.format("%d", days);
                        String countHours = String.format("%d", hours);
                        String countMin = String.format("%d", minutes);
                        String countSec = String.format("%d", seconds);

                        String concatenar = countDays + countHours + countMin;

                        if (!hourAlarm && concatenar.equals("010")) {
                            String tituloHora = Utility.getString(context, "notHoraTitle", "");
                            String contenidoHora = Utility.getString(context, "notHoraContent", "");
                            createNoti(tituloHora, contenidoHora);
                            hourAlarm = true;
                        }
                        if (!minuteAlarm && concatenar.equals("0030")) {
                            String tituloMed = Utility.getString(context, "notMedTitle", "");
                            String contenidoMed = Utility.getString(context, "notMedContent", "");
                            createNoti(tituloMed, contenidoMed);
                            minuteAlarm = true;
                        }
                        if (!finalAlarm && concatenar.equals("000")) {
                            String titulo = Utility.getString(context, "notFinalTitle", "");
                            String contenido = Utility.getString(context, "notFinalContent", "");
                            createNoti(titulo, contenido);
                            finalAlarm = true;
                        }

                        sendBroadcast(bi);

                        //Update widget Elements.
                        updateWidget(countDays,countHours,countMin,countSec, titleWidget1,titleWidget2,titleWidget3);
                        isPlaying = false;


                    } else if (currentDate.after(futureDate)) {
                        bi.putExtra("countdown", "1");
                        sendBroadcast(bi);
                        Utility.putString(context, "jugando", "true");

                        isPlaying = true;
                        String day = Utility.getString(context, "daysRemaining", "");
                        String hour = Utility.getString(context, "hoursRemaining", "");
                        String min = Utility.getString(context, "minutesRemaining", "");
                        String sec = Utility.getString(context, "secRemaining", "");
                        String titleWidget1 = Utility.getString(context, "title1", "");
                        String titleWidget2 = Utility.getString(context, "title2", "");
                        String titleWidget3 = Utility.getString(context, "text1", "");
                        updateWidget(day,hour,min,sec, titleWidget1,titleWidget2,titleWidget3);

                        resetTimer();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        handler.postDelayed(runnable, 0);

        //Handler to update the data automatically
        final Integer frequency = 1000 * 60 * 30;//(30 minutes)
        final Handler handlerTask = new Handler();
        handlerTask.postDelayed(new Runnable() {
            public void run() {
                if(Utility.isNetworkAvailable(context))
                {
                    new getFechaTask(context).execute(url);
                }
                handlerTask.postDelayed(this, frequency); //now is every 2 minutes
            }
        }, frequency);

    }

    public void resetTimer(){
        hourAlarm = false;
        minuteAlarm = false;
        finalAlarm = false;
    }

    //Connect to remote widget Textviews and update them.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void updateWidget(String days, String hours, String minutes, String seconds,
                              String titleWidget1, String titleWidget2, String titleWidget3) throws ParseException {

        AppWidgetManager gm = AppWidgetManager.getInstance(TimerService.context);
        ComponentName thisWidget = new ComponentName(TimerService.context, timerWidget.class);
        RemoteViews views = new RemoteViews(TimerService.context.getPackageName(), R.layout.timer_widget);

        if (isPlaying) {
            //Hide counter and set RiverJugando visible
            views.setViewVisibility(R.id.riverWidgetTXT, View.VISIBLE);
            //Counter
            views.setViewVisibility(R.id.dayCount, View.GONE);
            views.setViewVisibility(R.id.hourCount, View.GONE);
            views.setViewVisibility(R.id.minCount, View.GONE);
            views.setViewVisibility(R.id.secondCount, View.GONE);
            views.setViewVisibility(R.id.widgetDiaText, View.GONE);
            views.setViewVisibility(R.id.widgetHoraText, View.GONE);
            views.setViewVisibility(R.id.widgetMinText, View.GONE);
            views.setViewVisibility(R.id.widgetSecText, View.GONE);
            //Mid text
            views.setViewVisibility(R.id.tvText1, View.GONE);


        } else {
            //Set them back visible and hide RiverTXT
            views.setViewVisibility(R.id.riverWidgetTXT, View.GONE);
            //Counter
            views.setViewVisibility(R.id.dayCount, View.VISIBLE);
            views.setViewVisibility(R.id.hourCount, View.VISIBLE);
            views.setViewVisibility(R.id.minCount, View.VISIBLE);
            views.setViewVisibility(R.id.secondCount, View.VISIBLE);
            views.setViewVisibility(R.id.widgetDiaText, View.VISIBLE);
            views.setViewVisibility(R.id.widgetHoraText, View.VISIBLE);
            views.setViewVisibility(R.id.widgetMinText, View.VISIBLE);
            views.setViewVisibility(R.id.widgetSecText, View.VISIBLE);
            //Mid text
            views.setViewVisibility(R.id.tvText1, View.VISIBLE);


            views.setTextViewText(R.id.dayCount, days);
            views.setTextViewText(R.id.hourCount, hours);
            views.setTextViewText(R.id.minCount, minutes);
            views.setTextViewText(R.id.secondCount, seconds);

            views.setTextViewText(R.id.tvTitle1, titleWidget1);
            views.setTextViewText(R.id.tvTitle2, titleWidget2);
            views.setTextViewText(R.id.tvText1, titleWidget3);


            if (days.equals("1")) {
                views.setTextViewText(R.id.widgetDiaText, "Dia");
            } else {
                views.setTextViewText(R.id.widgetDiaText, "Dias");
            }

            if (hours.equals("1")) {
                views.setTextViewText(R.id.widgetHoraText, "Hora");
            } else {
                views.setTextViewText(R.id.widgetHoraText, "Horas");
            }

        }

        gm.updateAppWidget(thisWidget, views);

    }




    public void createNoti(String title, String content){
        Uri soundCancha = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hoy_river_app_ringtone);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.big_notificationicon)
                        .setContentTitle(title)
                        .setSound(soundCancha)
                        .setContentText(content);
        int NOTIFICATION_ID = 12345;
        Intent targetIntent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}
