package com.xpconversions.riverapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rramirez on 10/17/16.
 */
//THis class recreates the service AFTER rebooting the device
public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, TimerService.class);
            context.startService(pushIntent);
        }
    }
}