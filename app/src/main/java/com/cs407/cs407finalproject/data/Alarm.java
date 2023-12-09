package com.cs407.cs407finalproject.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.cs407.cs407finalproject.AlarmBroadcastReceiver;

import java.util.Calendar;

public class Alarm {
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

    // Setters
    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChallengeType(int challengeType) {
        this.challengeType = challengeType;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
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

        //Passing all alarm data
        intent.putExtra("ALARM_ID", alarm.alarmId);
        intent.putExtra("RECURRING", alarm.isRecurring);
        intent.putExtra("MONDAY", alarm.monday);
        intent.putExtra("TUESDAY", alarm.tuesday);
        intent.putExtra("WEDNESDAY", alarm.wednesday);
        intent.putExtra("THURSDAY", alarm.thursday);
        intent.putExtra("FRIDAY", alarm.friday);
        intent.putExtra("SATURDAY", alarm.saturday);
        intent.putExtra("SUNDAY", alarm.sunday);
        intent.putExtra("TITLE", alarm.title);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, alarm.getAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long alarmStartTime = getAlarmStartTime(alarm);

        if (alarm.isRecurring) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime,
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
        }
    }
}
