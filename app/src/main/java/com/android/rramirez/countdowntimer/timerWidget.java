package com.android.rramirez.countdowntimer;

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
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class timerWidget extends AppWidgetProvider {

    private static int WIDGET_REFRESH_TIME = 1;
    public Handler handler = new Handler();
    public Intent intent;

    private Context ctx;

    Runnable updateWidgetText = new Runnable()
    {
        @Override
        public void run() {
            AppWidgetManager gm = AppWidgetManager.getInstance(ctx);
            ComponentName thisWidget = new ComponentName(ctx, timerWidget.class);

            try{
                //Fetch fecha from SharedPref
                String fetchFecha = Utility.getString(ctx, "fecha", "");
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

                    RemoteViews views = new RemoteViews(ctx.getPackageName(), R.layout.timer_widget);
                    views.setTextViewText(R.id.dayCount,  String.format("%d", days));
                    views.setTextViewText(R.id.hourCount, String.format("%d", hours));
                    views.setTextViewText(R.id.minCount, String.format("%d", minutes));
                    views.setTextViewText(R.id.secondCount, String.format("%d", seconds));

                    gm.updateAppWidget(thisWidget, views);

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            handler.postDelayed(this, WIDGET_REFRESH_TIME * 1000);

        }
    };

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
            Utility.makeToast(ctx, "getting GUI");
            Log.i("APP GUI", "Registered to Service");


        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        intent = new Intent(context, TimerService.class);

        String title1 = Utility.getString(context, "title1", "");
        String title2 = Utility.getString(context, "title2", "");
        String text1 = Utility.getString(context, "text1", "");

        ComponentName thisWidget = new ComponentName(context, timerWidget.class);
        for (int widgetId : appWidgetManager.getAppWidgetIds(thisWidget)) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_widget);

            views.setTextViewText(R.id.tvTitle1, title1);
            views.setTextViewText(R.id.tvTitle2, title2);
            views.setTextViewText(R.id.tvText1, text1);
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

        try {
            context.getApplicationContext().registerReceiver(br, new IntentFilter(TimerService.COUNTDOWN_BR));} catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("android.appwidget.action.APPWIDGET_ENABLED")) {
            handler.removeCallbacks(updateWidgetText);
            ctx = context;
             handler.postDelayed(updateWidgetText, WIDGET_REFRESH_TIME*1000);
        }
        else if (intent.getAction().equals("android.appwidget.action.APPWIDGET_DISABLED")) {
            handler.removeCallbacks(updateWidgetText);
        }

    }

    @Override
    public void onDisabled(Context context) {
//        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
//        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

    }


}


