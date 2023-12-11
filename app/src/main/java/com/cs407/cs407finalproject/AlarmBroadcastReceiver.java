package com.cs407.cs407finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cs407.cs407finalproject.data.Alarm;
import com.cs407.cs407finalproject.data.AlarmDBHelper;

import java.util.Calendar;

/**
 * Allows Alarms to interact with other intents and contexts
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    /**
     * method overridden from BroadcastReceiver
     *
     * @param context context object
     * @param intent intent object
     */
    @Override
    public void onReceive(Context context, Intent intent) {
            if ((!intent.getBooleanExtra("RECURRING", false)
                    || alarmIsToday(intent))
                    && intent.getBooleanExtra("IS_ON", true)) {
                startAlarmService(context, intent);
            }

    }

    /**
     * Checks whether the alarm is set for today
     *
     * @param intent Intent object
     * @return boolean, whether the alarm is set for today
     */
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

    /**
     * starts the alarm notification service
     *
     * @param context Context object
     * @param intent Intent object
     */
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