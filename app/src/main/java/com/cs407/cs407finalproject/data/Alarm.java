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

/**
 * Alarm class, implementing Serializable, creates an Alarm object.
 */
public class Alarm implements Serializable, Comparable<Alarm> {
    //Contains the ID for the Alarm
    private int alarmId;
    //The hour the alarm is set for
    private int hour;
    //The exact minute the alarm is set for
    private int minute;
    //whether the alarm is recurring or not
    private boolean isRecurring;
    //whether the alarm is on
    private boolean isOn;
    //booleans for all 7 days of the week
    private boolean monday, tuesday, wednesday,
            thursday, friday, saturday, sunday;
    //type of challenge used for this alarm
    private int challengeType;
    //title of the alarm
    private String title;

    /**
     * Sets all the variables for the alarm
     *
     * @param alarmId id of the alarm
     * @param hour hour of the alarm
     * @param minute minute of the alarm
     * @param title name of the alarm
     * @param challengeType the type of challenge the user will get
     * @param isOn whether the alarm is on
     * @param isRecurring whether the alarm is recurring
     * @param monday whether the alarm is for monday
     * @param tuesday whether the alarm is for tuesday
     * @param wednesday whether the alarm is for wednesday
     * @param thursday whether the alarm is for thursday
     * @param friday whether the alarm is for friday
     * @param saturday whether the alarm is for saturday
     * @param sunday whether the alarm is for sunday
     */
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

    /**
     * Returns the ID for this alarm.
     *
     * @return the id for this alarm
     */
    public int getAlarmId() {
        return alarmId;
    }

    /**
     * Returns the hour the alarm goes off.
     *
     * @return int, the hour the alarm will go off
     */
    public int getHour() {
        return hour;
    }

    /**
     * Returns the minute the alarm goes off.
     *
     * @return int, the minute the alarm will go off
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Returns the title of the alarm
     *
     * @return String, The title of the alarm
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the challenge type for this alarm
     *
     * @return int, to determine which challenge
     */
    public int getChallengeType() {
        return challengeType;
    }

    /**
     * Determines whether this alarm is on
     *
     * @return boolean, whether the alarm is on
     */
    public boolean isOn() {
        return isOn;
    }

    /**
     * Determines whether this alarm is a recurring alarm
     *
     * @return boolean, whether the alarm recurs or not.
     */
    public boolean isRecurring() {
        return isRecurring;
    }

    /**
     * Determines whether this alarm is on the specified day
     *
     * @return boolean, whether the alarm is for Monday
     */
    public boolean isMonday() {
        return monday;
    }

    /**
     * Determines whether this alarm is on the specified day
     *
     * @return boolean, whether the alarm is for Tuesday
     */
    public boolean isTuesday() {
        return tuesday;
    }

    /**
     * Determines whether this alarm is on the specified day
     *
     * @return boolean, whether the alarm is for Wednesday
     */
    public boolean isWednesday() {
        return wednesday;
    }

    /**
     * Determines whether this alarm is on the specified day
     *
     * @return boolean, whether the alarm is for Thursday
     */
    public boolean isThursday() {
        return thursday;
    }

    /**
     * Determines whether this alarm is on the specified day
     *
     * @return boolean, whether the alarm is for Friday
     */
    public boolean isFriday() {
        return friday;
    }

    /**
     * Determines whether this alarm is on the specified day
     *
     * @return boolean, whether the alarm is for Saturday
     */
    public boolean isSaturday() {
        return saturday;
    }

    /**
     * Determines whether this alarm is on the specified day
     *
     * @return boolean, whether the alarm is for Sunday
     */
    public boolean isSunday() {
        return sunday;
    }

    /**
     * Returns the start time of the alarm
     *
     * @param alarm The Alarm object
     * @return returns a long containing the start time of the alarm
     */
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

    /**
     *
     *
     * @param alarm Alarm object to access the date of the alarm.
     * @param calendar Calendar object to get the current day of the week.
     * @return boolean, Whether the alarm date is today
     */
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

    /**
     * Allows user to schedule a new alarm
     *
     * @param context Context object
     * @param alarm Alarm to be scheduled
     */
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


    /**
     * Cancels an Alarm.
     *
     * @param context Context object
     * @param alarm Alarm to be cancelled
     */
    public static void cancelAlarm(Context context, Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getAlarmId(), intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public String toString(){
        String title = "untitled";
        if (this.getTitle().length() > 0) {
            title = this.getTitle();
        }
        if (this.isOn()) {
            return "[ON] " + this.getHour() + ":" + String.format("%02d", this.getMinute())
                    + " - " + title;
        }
        return "[OFF] " + this.getHour() + ":" + String.format("%02d", this.getMinute())
                + " - " + title;
    }

    @Override
    public int compareTo(Alarm other) {
        int hourCompare = Integer.compare(this.getHour(), other.getHour());
        if (hourCompare == 0) {
            return Integer.compare(this.getMinute(), other.getMinute());
        }
        return hourCompare;
    }
}