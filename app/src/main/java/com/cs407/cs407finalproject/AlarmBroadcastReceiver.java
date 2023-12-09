package com.cs407.cs407finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Check if the alarm is recurring and if today is one of the scheduled days
            if (!intent.getBooleanExtra("RECURRING", false) || alarmIsToday(intent)) {
                startAlarmService(context, intent);
            }
        }
    }

    private boolean alarmIsToday(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (intent.getBooleanExtra("MONDAY", false))
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (intent.getBooleanExtra("TUESDAY", false))
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (intent.getBooleanExtra("WEDNESDAY", false))
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (intent.getBooleanExtra("THURSDAY", false))
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (intent.getBooleanExtra("FRIDAY", false))
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (intent.getBooleanExtra("SATURDAY", false))
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (intent.getBooleanExtra("SUNDAY", false))
                    return true;
                return false;
        }
        return false;
    }

    private void startAlarmService(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("TITLE", intent.getStringExtra("TITLE"));
        context.startService(serviceIntent);
    }
}
