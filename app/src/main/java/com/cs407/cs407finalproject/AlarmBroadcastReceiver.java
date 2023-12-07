package com.cs407.cs407finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        // Start an activity or send a notification here
        int alarmId = intent.getIntExtra("ALARM_ID", -1);
        // Handle the alarm event based on the alarmId or other data passed

    }
}
