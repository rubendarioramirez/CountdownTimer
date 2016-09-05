package com.android.rramirez.countdowntimer;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class timerWidget extends AppWidgetProvider {

    public Intent intent;
    private Context ctx;

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


