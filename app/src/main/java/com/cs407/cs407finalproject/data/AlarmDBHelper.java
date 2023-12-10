package com.cs407.cs407finalproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

//https://developer.android.com/training/data-storage/sqlite
public class AlarmDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AlarmDatabase.db";
    private static final String TABLE_NAME = "alarms";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HOUR = "hour";
    private static final String COLUMN_MINUTE = "minute";
    private static final String COLUMN_IS_RECURRING = "isRecurring";
    private static final String COLUMN_IS_ON = "isOn";
    private static final String COLUMN_MONDAY = "monday";
    private static final String COLUMN_TUESDAY = "tuesday";
    private static final String COLUMN_WEDNESDAY = "wednesday";
    private static final String COLUMN_THURSDAY = "thursday";
    private static final String COLUMN_FRIDAY = "friday";
    private static final String COLUMN_SATURDAY = "saturday";
    private static final String COLUMN_SUNDAY = "sunday";
    private static final String COLUMN_CHALLENGE_TYPE = "challengeType";
    private static final String COLUMN_TITLE = "title";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_HOUR + " INTEGER," +
                    COLUMN_MINUTE + " INTEGER," +
                    COLUMN_IS_RECURRING + " INTEGER," +
                    COLUMN_IS_ON + " INTEGER," +
                    COLUMN_MONDAY + " INTEGER," +
                    COLUMN_TUESDAY + " INTEGER," +
                    COLUMN_WEDNESDAY + " INTEGER," +
                    COLUMN_THURSDAY + " INTEGER," +
                    COLUMN_FRIDAY + " INTEGER," +
                    COLUMN_SATURDAY + " INTEGER," +
                    COLUMN_SUNDAY + " INTEGER," +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_CHALLENGE_TYPE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public AlarmDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long insertAlarm(@NonNull Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // Don't put id, as it's autoincrement
        values.put(COLUMN_HOUR, alarm.getHour());
        values.put(COLUMN_MINUTE, alarm.getMinute());

        values.put(COLUMN_IS_RECURRING, alarm.isRecurring() ? 1 : 0);
        values.put(COLUMN_IS_ON, alarm.isOn() ? 1 : 0);
        values.put(COLUMN_MONDAY, alarm.isMonday() ? 1 : 0);
        values.put(COLUMN_TUESDAY, alarm.isTuesday() ? 1 : 0);
        values.put(COLUMN_WEDNESDAY, alarm.isWednesday() ? 1 : 0);
        values.put(COLUMN_THURSDAY, alarm.isThursday() ? 1 : 0);
        values.put(COLUMN_FRIDAY, alarm.isFriday() ? 1 : 0);
        values.put(COLUMN_SATURDAY, alarm.isSaturday() ? 1 : 0);
        values.put(COLUMN_SUNDAY, alarm.isSunday() ? 1 : 0);

        values.put(COLUMN_TITLE, alarm.getTitle());
        values.put(COLUMN_CHALLENGE_TYPE, alarm.getChallengeType());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        // returns -1 if there was an error
        return id;
    }

    public int updateAlarm(@NonNull Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HOUR, alarm.getHour());
        values.put(COLUMN_MINUTE, alarm.getMinute());

        values.put(COLUMN_IS_RECURRING, alarm.isRecurring() ? 1 : 0);
        values.put(COLUMN_IS_ON, alarm.isOn() ? 1 : 0);
        values.put(COLUMN_MONDAY, alarm.isMonday() ? 1 : 0);
        values.put(COLUMN_TUESDAY, alarm.isTuesday() ? 1 : 0);
        values.put(COLUMN_WEDNESDAY, alarm.isWednesday() ? 1 : 0);
        values.put(COLUMN_THURSDAY, alarm.isThursday() ? 1 : 0);
        values.put(COLUMN_FRIDAY, alarm.isFriday() ? 1 : 0);
        values.put(COLUMN_SATURDAY, alarm.isSaturday() ? 1 : 0);
        values.put(COLUMN_SUNDAY, alarm.isSunday() ? 1 : 0);

        values.put(COLUMN_TITLE, alarm.getTitle());
        values.put(COLUMN_CHALLENGE_TYPE, alarm.getChallengeType());

        int count = db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(alarm.getAlarmId()) });
        db.close();

        // returns method is the number of rows affected in the database.
        return count;
    }

    public void deleteAlarm(int alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[] { String.valueOf(alarmId) });
        db.close();
    }

    public Alarm getAlarmById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int alarmId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            int hour = cursor.getInt(cursor.getColumnIndex(COLUMN_HOUR));
            int minute = cursor.getInt(cursor.getColumnIndex(COLUMN_MINUTE));
            boolean isRecurring = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_RECURRING)) == 1;
            boolean isOn = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ON)) == 1;

            boolean monday = cursor.getInt(cursor.getColumnIndex(COLUMN_MONDAY)) == 1;

            boolean tuesday = cursor.getInt(cursor.getColumnIndex(COLUMN_TUESDAY)) == 1;
            boolean wednesday = cursor.getInt(cursor.getColumnIndex(COLUMN_WEDNESDAY)) == 1;
            boolean thursday = cursor.getInt(cursor.getColumnIndex(COLUMN_THURSDAY)) == 1;
            boolean friday = cursor.getInt(cursor.getColumnIndex(COLUMN_FRIDAY)) == 1;
            boolean saturday = cursor.getInt(cursor.getColumnIndex(COLUMN_SATURDAY)) == 1;

            boolean sunday = cursor.getInt(cursor.getColumnIndex(COLUMN_SUNDAY)) == 1;

            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            int challengeType = cursor.getInt(cursor.getColumnIndex(COLUMN_CHALLENGE_TYPE));

            Alarm alarm = new Alarm(alarmId, hour, minute, title, challengeType, isOn, isRecurring,
                    monday, tuesday, wednesday, thursday, friday, saturday, sunday);
            cursor.close();
            db.close();
            return alarm;
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null; // Return null if alarm is not found
    }

    public List<Alarm> getAllAlarms() {
        List<Alarm> alarms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int alarmId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                int hour = cursor.getInt(cursor.getColumnIndex(COLUMN_HOUR));
                int minute = cursor.getInt(cursor.getColumnIndex(COLUMN_MINUTE));
                boolean isRecurring = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_RECURRING)) == 1; // 1 is true, 0 is false
                boolean isOn = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ON)) == 1;
                boolean monday = cursor.getInt(cursor.getColumnIndex(COLUMN_MONDAY)) == 1;

                boolean tuesday = cursor.getInt(cursor.getColumnIndex(COLUMN_TUESDAY)) == 1;
                boolean wednesday = cursor.getInt(cursor.getColumnIndex(COLUMN_WEDNESDAY)) == 1;
                boolean thursday = cursor.getInt(cursor.getColumnIndex(COLUMN_THURSDAY)) == 1;
                boolean friday = cursor.getInt(cursor.getColumnIndex(COLUMN_FRIDAY)) == 1;
                boolean saturday = cursor.getInt(cursor.getColumnIndex(COLUMN_SATURDAY)) == 1;

                boolean sunday = cursor.getInt(cursor.getColumnIndex(COLUMN_SUNDAY)) == 1;

                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                int challengeType = cursor.getInt(cursor.getColumnIndex(COLUMN_CHALLENGE_TYPE));

                Alarm alarm = new Alarm(alarmId, hour, minute, title, challengeType, isOn, isRecurring,
                        monday, tuesday, wednesday, thursday, friday, saturday, sunday);

                alarms.add(alarm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alarms;
    }

    public void updateAlarmStatus(int alarmId, boolean isEnabled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_ON, isEnabled);
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(alarmId) });
    }
}