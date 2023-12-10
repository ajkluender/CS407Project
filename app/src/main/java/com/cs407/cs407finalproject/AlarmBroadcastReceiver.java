package com.cs407.cs407finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "onReceive triggered");

        // Check if the alarm is non-recurring or if it's recurring and today is one of the alarm days
        if (!intent.getBooleanExtra("RECURRING", false) || alarmIsToday(intent)) {
            Log.d("AlarmReceiver", "Starting Alarm Service");
            // Start the alarm service to handle the alarm event
            startAlarmService(context, intent);
        }
    }

    private boolean alarmIsToday(Intent intent) {

        // Get the current day of the week
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        // Check if today matches the scheduled day(s) for the alarm
        switch(today) {

            case Calendar.MONDAY: return intent.getBooleanExtra("MONDAY", false);
            case Calendar.TUESDAY: return intent.getBooleanExtra("TUESDAY", false);
            case Calendar.WEDNESDAY: return intent.getBooleanExtra("WEDNESDAY", false);
            case Calendar.THURSDAY: return intent.getBooleanExtra("THURSDAY", false);
            case Calendar.FRIDAY: return intent.getBooleanExtra("FRIDAY", false);
            case Calendar.SATURDAY: return intent.getBooleanExtra("SATURDAY", false);
            case Calendar.SUNDAY: return intent.getBooleanExtra("SUNDAY", false);
            default: return false;
        }
    }

    private void startAlarmService(Context context, Intent intent) {
        // Creates an intent to start the AlarmService
        Intent serviceIntent = new Intent(context, AlarmService.class);

        // Add alarm details to the intent
        serviceIntent.putExtra("TITLE", intent.getStringExtra("TITLE"));
        serviceIntent.putExtra("ALARM_ID", intent.getIntExtra("ALARM_ID", 0));
        serviceIntent.putExtra("CHALLENGE", intent.getIntExtra("CHALLENGE", 0));

        // Start the service
        context.startService(serviceIntent);
    }
}
