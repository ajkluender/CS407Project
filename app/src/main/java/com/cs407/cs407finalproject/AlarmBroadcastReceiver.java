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
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if ((!intent.getBooleanExtra("RECURRING", false)
                    || alarmIsToday(intent))
                    && intent.getBooleanExtra("IS_ON", true)) {
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
        serviceIntent.putExtra("ALARM", intent.getSerializableExtra("ALARM"));
        serviceIntent.putExtra("TITLE", intent.getStringExtra("TITLE"));
        serviceIntent.putExtra("ALARM_ID", intent.getIntExtra("ALARM_ID", 0));
        serviceIntent.putExtra("CHALLENGE", intent.getIntExtra("CHALLENGE", 0));
        serviceIntent.putExtra("RECURRING", intent.getBooleanExtra("RECURRING", false));
        context.startService(serviceIntent);
    }
}