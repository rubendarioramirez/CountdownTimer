package com.android.rramirez.countdowntimer;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.android.rramirez.countdowntimer.Utility.getDate;

/**
 * Implementation of App Widget functionality.
 */
public class timerWidget extends AppWidgetProvider {

    private Intent intent;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        intent = new Intent(context, TimerService.class);

        String title1 = Utility.getString(context, "title1", "");
        String title2 = Utility.getString(context, "title2", "");
        String text1 = Utility.getString(context, "text1", "");
        String text2 = Utility.getString(context, "text2", "");
        String fetchFecha = Utility.getString(context, "longMilis", "");

        ComponentName thisWidget = new ComponentName(context, timerWidget.class);
        for (int widgetId : appWidgetManager.getAppWidgetIds(thisWidget)) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_widget);

            //Parse the string to Long.
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

                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            views.setTextViewText(R.id.tvTitle1, title1);
            views.setTextViewText(R.id.tvTitle2, title2);
            views.setTextViewText(R.id.tvText1, text1);
            views.setTextViewText(R.id.tvText2, text2);
            appWidgetManager.updateAppWidget(appWidgetIds, views);
        }
    }
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After 3 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, pi);
    }

    @Override
    public void onDisabled(Context context) {
        Toast.makeText(context, "onDisabled(): last widget instance removed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Toast.makeText(context, "CountDownTimerWidget removed id(s):" + appWidgetIds, Toast.LENGTH_SHORT).show();
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Toast.makeText(context, "onAppWidgetOptionsChanged() called", Toast.LENGTH_SHORT).show();
    }


}


