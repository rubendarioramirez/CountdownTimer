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
        String fetchFecha = Utility.getString(context,"longMilis","");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_widget);

                    String day = Utility.getString(context,"daysRemaining", "");
                    String hour = Utility.getString(context,"hoursRemaining", "");
                    String min = Utility.getString(context,"minutesRemaining", "");
                    String sec = Utility.getString(context,"secRemaining", "");
                    views.setTextViewText(R.id.dayCount, day);
                    views.setTextViewText(R.id.hourCount, hour);
                    views.setTextViewText(R.id.minCount, min);
                    views.setTextViewText(R.id.segCount, sec);
                    views.setTextViewText(R.id.tvTitle1, title1);
                    views.setTextViewText(R.id.tvTitle2, title2);
                    views.setTextViewText(R.id.tvText1, text1);
                    ComponentName thisWidget = new ComponentName(context, timerWidget.class);
                    AppWidgetManager manager = AppWidgetManager.getInstance(context);
                    manager.updateAppWidget(thisWidget, views);
                    wl.release();
                }

            }

