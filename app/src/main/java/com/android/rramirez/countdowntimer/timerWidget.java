package com.android.rramirez.countdowntimer;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class timerWidget extends AppWidgetProvider {


      public Intent intent;
      private Context ctx;


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
        Log.i("CONTEXT 1", TimerService.context.toString());
        if (intent.getExtras() != null) {

          //  Utility.makeToast(TimerService.context, "getting GUI");
            Log.i("CONTEXT 2", TimerService.context.toString());

            AppWidgetManager gm = AppWidgetManager.getInstance(TimerService.context);
            ComponentName thisWidget = new ComponentName(TimerService.context, timerWidget.class);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Fetch fecha from SharedPref
            String fetchFecha = Utility.getString(TimerService.context, "fecha", "");
            Date futureDate = dateFormat.parse(fetchFecha);
            Date currentDate = new Date();
            if (!currentDate.after(futureDate)) {
                long diff = futureDate.getTime() - currentDate.getTime();
                long days = diff / (24 * 60 * 60 * 1000);
                diff -= days * (24 * 60 * 60 * 1000);
                long hours = diff / (60 * 60 * 1000);
                diff -= hours * (60 * 60 * 1000);
                long minutes = diff / (60 * 1000);
                diff -= minutes * (60 * 1000);
                long seconds = diff / 1000;

                RemoteViews views = new RemoteViews(TimerService.context.getPackageName(), R.layout.timer_widget);
                views.setTextViewText(R.id.dayCount, String.format("%d", days));
                views.setTextViewText(R.id.hourCount, String.format("%d", hours));
                views.setTextViewText(R.id.minCount, String.format("%d", minutes));
                views.setTextViewText(R.id.secondCount, String.format("%d", seconds));

                gm.updateAppWidget(thisWidget, views);

            }

        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(TimerService.context, appWidgetManager, appWidgetIds);

        String title1 = Utility.getString(TimerService.context, "title1", "");
        String title2 = Utility.getString(TimerService.context, "title2", "");
        String text1 = Utility.getString(TimerService.context, "text1", "");

        ComponentName thisWidget = new ComponentName(TimerService.context, timerWidget.class);
        for (int widgetId : appWidgetManager.getAppWidgetIds(thisWidget)) {
            RemoteViews views = new RemoteViews(TimerService.context.getPackageName(), R.layout.timer_widget);

            views.setTextViewText(R.id.tvTitle1, title1);
            views.setTextViewText(R.id.tvTitle2, title2);
            views.setTextViewText(R.id.tvText1, text1);
            appWidgetManager.updateAppWidget(appWidgetIds, views);
        }

    }

    @Override
    public void onEnabled(Context context) {
       super.onEnabled(TimerService.context);
        intent = new Intent(TimerService.context, TimerService.class);

        try {
            TimerService.context.registerReceiver(br, new IntentFilter(TimerService.COUNTDOWN_BR));} catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(TimerService.context, intent);


    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(TimerService.context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(TimerService.context, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(TimerService.context, appWidgetManager, appWidgetId, newOptions);

    }


}


