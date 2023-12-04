package com.cs407.cs407finalproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//https://developer.android.com/training/data-storage/sqlite
public class AlarmDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AlarmDatabase.db";
//DAYS
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
    //END DAYS
    private static final String COLUMN_CHALLENGE_TYPE = "challengeType";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_HOUR + " INTEGER," +
                    COLUMN_MINUTE + " INTEGER," +
                    COLUMN_IS_RECURRING + " BOOLEAN," +
                    COLUMN_IS_ON + " BOOLEAN," +
                    COLUMN_MONDAY + " BOOLEAN," +
                    COLUMN_TUESDAY + " BOOLEAN," +
                    COLUMN_WEDNESDAY + " BOOLEAN," +
                    COLUMN_THURSDAY + " BOOLEAN," +
                    COLUMN_FRIDAY + " BOOLEAN," +
                    COLUMN_SATURDAY + " BOOLEAN," +
                    COLUMN_SUNDAY + " BOOLEAN," +
                    COLUMN_CHALLENGE_TYPE + " TEXT)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public AlarmDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // Add methods to insert, update, delete, and retrieve alarms
    
}
