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

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_widget);
        views.setTextViewText(R.id.tvClock, Utility.updateCurrentCountDownTime(context));

        ComponentName thisWidget = new ComponentName(context, timerWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, views);

        wl.release();
    }
}
