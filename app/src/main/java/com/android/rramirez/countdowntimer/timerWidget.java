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
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class timerWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        String title1 = Utility.getString(context,"title1","");
        String title2 = Utility.getString(context,"title2","");
        String text1 = Utility.getString(context,"text1","");
        String text2 = Utility.getString(context,"text2","");

        ComponentName thisWidget = new ComponentName(context, timerWidget.class);

        for(int widgetId : appWidgetManager.getAppWidgetIds(thisWidget)) {

            //Get the date and split it
           String date = Utility.getString(context,"fecha","");
            String[] separated = date.split(":");


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_widget);
            views.setTextViewText(R.id.dayCount, separated[0]);
            views.setTextViewText(R.id.hourCount, separated[1]);
            views.setTextViewText(R.id.minCount, separated[2]);
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


