package com.cs407.cs407finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cs407.cs407finalproject.data.Alarm;
import com.cs407.cs407finalproject.data.AlarmDBHelper;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            if ((!intent.getBooleanExtra("RECURRING", false)
                    || alarmIsToday(intent))
                    && intent.getBooleanExtra("IS_ON", true)) {
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
        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("ALARM", intent.getSerializableExtra("ALARM"));
        serviceIntent.putExtra("TITLE", intent.getStringExtra("TITLE"));
        serviceIntent.putExtra("ALARM_ID", intent.getIntExtra("ALARM_ID", 0));
        serviceIntent.putExtra("CHALLENGE", intent.getIntExtra("CHALLENGE", 0));
        serviceIntent.putExtra("RECURRING", intent.getBooleanExtra("RECURRING", false));

        context.startService(serviceIntent);
    }
}