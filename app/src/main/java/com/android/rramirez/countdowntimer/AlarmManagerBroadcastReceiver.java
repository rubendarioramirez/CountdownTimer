package com.android.rramirez.countdowntimer;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.RemoteViews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.android.rramirez.countdowntimer.Utility.getDate;
import static java.lang.String.valueOf;

/**
 * Created by gusta_000 on 24/8/2016.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = AlarmManagerBroadcastReceiver.class.getSimpleName();
    private  Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {

        intent = new Intent(context, TimerService.class);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wl.acquire();

        //Get title from Shared preferences
        String title1 = Utility.getString(context,"title1","");
        String title2 = Utility.getString(context,"title2","");
        String text1 = Utility.getString(context,"text1","");
        String text2 = Utility.getString(context,"text2","");
        String fetchFecha = Utility.getString(context,"longMilis","");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_widget);
            long l = Long.parseLong(fetchFecha);

            //Convert the LONG to a date
            String fecha = getDate(l, "dd/MM/yyyy hh:mm:ss");
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

                    views.setTextViewText(R.id.dayCount, String.format("%02d", days));
                    views.setTextViewText(R.id.hourCount, String.format("%02d", hours));
                    views.setTextViewText(R.id.minCount, String.format("%02d", minutes));
                    views.setTextViewText(R.id.tvTitle1, title1);
                    views.setTextViewText(R.id.tvTitle2, title2);
                    views.setTextViewText(R.id.tvText1, text1);
                    views.setTextViewText(R.id.tvText2, text2);
                    ComponentName thisWidget = new ComponentName(context, timerWidget.class);
                    AppWidgetManager manager = AppWidgetManager.getInstance(context);
                    manager.updateAppWidget(thisWidget, views);

                    wl.release();
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

