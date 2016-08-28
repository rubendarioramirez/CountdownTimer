package com.android.rramirez.countdowntimer;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.RemoteViews;

/**
 * Created by gusta_000 on 24/8/2016.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = AlarmManagerBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wl.acquire();

        //Get title from Shared preferences
        String title1 = Utility.getString(context,"title1","");
        String title2 = Utility.getString(context,"title2","");
        String text1 = Utility.getString(context,"text1","");
        String text2 = Utility.getString(context,"text2","");

        //Get the date and split
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

        ComponentName thisWidget = new ComponentName(context, timerWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, views);

        wl.release();
    }
}
