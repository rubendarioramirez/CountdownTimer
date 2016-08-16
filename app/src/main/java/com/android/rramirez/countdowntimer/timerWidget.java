package com.android.rramirez.countdowntimer;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Handler;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class timerWidget extends AppWidgetProvider {

    private Handler handler;
    private Runnable runnable;
    private TextView tvDia, tvHora, tvMin, tvSeg;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }

        countDownStart("2016-01-01 08:05:00");
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timer_widget);
        views.setTextViewText(R.id.tvDia, "hola");
        views.setTextViewText(R.id.tvHora, "hola");
        views.setTextViewText(R.id.tvMin, "hola");
        views.setTextViewText(R.id.tvSeg, "hola");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    // //////////////COUNT DOWN START/////////////////////////
    public void countDownStart(String eventDate) {
        final String fetchFecha = eventDate;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
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
                        long minutes = diff/(60*1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        tvDia.setText("" + String.format("%02d", days));
                        tvHora.setText("" + String.format("%02d", hours));
                        tvMin.setText("" + String.format("%02d", minutes));
                        tvSeg.setText("" + String.format("%02d", seconds));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

}

