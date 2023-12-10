package com.cs407.cs407finalproject.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cs407.cs407finalproject.AlarmBroadcastReceiver;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Alarm implements Serializable {
    private int alarmId;
    private int hour;
    private int minute;
    private boolean isRecurring;
    private boolean isOn;
    private boolean monday, tuesday, wednesday,
            thursday, friday, saturday, sunday;
    private int challengeType;
    private String title;

    // Constructor
    public Alarm(int alarmId, int hour, int minute, String title,
                 int challengeType, boolean isOn, boolean isRecurring, boolean monday, boolean tuesday,
                 boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday){
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;

        this.title = title;
        this.challengeType = challengeType;
        this.isRecurring = isRecurring;
        this.isOn = isOn;

        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }
    // Getters
    public int getAlarmId() {
        return alarmId;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getTitle() {
        return title;
    }

    public int getChallengeType() {
        return challengeType;
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    private static long getAlarmStartTime(Alarm alarm) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // If the alarm is recurring, find the next occurrence
        if (alarm.isRecurring()) {
            while (calendar.getTimeInMillis() <= System.currentTimeMillis() || !isAlarmDayToday(alarm, calendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else {
            // If the set time has already passed today, set it for the next day
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return calendar.getTimeInMillis();
    }

    private static boolean isAlarmDayToday(Alarm alarm, Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.MONDAY: return alarm.isMonday();
            case Calendar.TUESDAY: return alarm.isTuesday();
            case Calendar.WEDNESDAY: return alarm.isWednesday();
            case Calendar.THURSDAY: return alarm.isThursday();
            case Calendar.FRIDAY: return alarm.isFriday();
            case Calendar.SATURDAY: return alarm.isSaturday();
            case Calendar.SUNDAY: return alarm.isSunday();
            default: return false;
        }
    }
    public static void scheduleAlarm(Context context, Alarm alarm){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);

        Log.d("Alarm", "Scheduling alarm: " + alarm.getTitle() + " at " + alarm.getHour() + ":" + alarm.getMinute());

        //Passing all alarm data
        intent.putExtra("ALARM", alarm);
        intent.putExtra("ALARM_ID", alarm.alarmId);
        intent.putExtra("IS_ON", alarm.isOn);
        intent.putExtra("RECURRING", alarm.isRecurring);
        intent.putExtra("MONDAY", alarm.monday);
        intent.putExtra("TUESDAY", alarm.tuesday);
        intent.putExtra("WEDNESDAY", alarm.wednesday);
        intent.putExtra("THURSDAY", alarm.thursday);
        intent.putExtra("FRIDAY", alarm.friday);
        intent.putExtra("SATURDAY", alarm.saturday);
        intent.putExtra("SUNDAY", alarm.sunday);
        intent.putExtra("TITLE", alarm.title);
        intent.putExtra("CHALLENGE", alarm.challengeType);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, alarm.getAlarmId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long alarmStartTime = getAlarmStartTime(alarm);
        Log.d("Alarm", "Alarm start time: " + new Date(alarmStartTime).toString());

        if (alarm.isRecurring) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime,
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
        }
    }

    public static void cancelAlarm(Context context, Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getAlarmId(), intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }
}