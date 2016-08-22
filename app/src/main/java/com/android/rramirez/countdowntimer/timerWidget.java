package com.android.rramirez.countdowntimer;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class timerWidget extends AppWidgetProvider {

    private static final String APPWIDGET_UPDATE = "com.android.rramirez.countdowntimer.APPWIDGET_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (APPWIDGET_UPDATE.endsWith(intent.getAction())) {
            update(context);
        } else super.onReceive(context, intent);
    }

    private void update(Context context) {
        ComponentName name = new ComponentName(context.getPackageName(), this.getClass().getName());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(name);
        onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent intent = new Intent(context, this.getClass());
        intent.setAction(timerWidget.APPWIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int interval = 60000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
       // manager.setRepeating(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), interval, pendingIntent);
        manager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, this.getClass());
        intent.setAction(timerWidget.APPWIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_widget);
            Calendar calendar = Calendar.getInstance();
            views.setTextViewText(R.id.tvClock, DateFormat.format("hh:mm aa", calendar.getTime()));
           // views.setTextViewText(R.id.clock_widget_date, DateFormat.format("MMM dd, yyyy", calendar.getTime()));
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}


