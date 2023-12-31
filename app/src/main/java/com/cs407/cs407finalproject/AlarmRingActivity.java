package com.cs407.cs407finalproject;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.cs407.cs407finalproject.data.Alarm;
import com.cs407.cs407finalproject.data.AlarmDBHelper;
import java.util.Calendar;

/**
 * AlarmRingActivity extends AppCompatActivity for the activity
 * Has the Activity for the UI that contains the alarm when it rings
 */
public class AlarmRingActivity extends AppCompatActivity {

    Alarm alarm;
    String title = "";
    int alarmId;
    int challengeType = -1;
    boolean isRecurring;
    private static final String CHANNEL_ID = "AlarmServiceChannel";

    /**
     * Overridden from AppCompactActivity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ALARM")) {
            alarm = (Alarm) intent.getSerializableExtra("ALARM");
        }
        if (intent != null && intent.hasExtra("TITLE")) {
            title = intent.getStringExtra("TITLE");
        }
        if (intent != null && intent.hasExtra("ALARM_ID")) {
            alarmId = intent.getIntExtra("ALARM_ID", -1);
        }
        if (intent != null && intent.hasExtra("CHALLENGE")) {
            challengeType = intent.getIntExtra("CHALLENGE", 0);
        }
        if (intent != null && intent.hasExtra("RECURRING")) {
            isRecurring = intent.getBooleanExtra("RECURRING", false);
        }

        if (!isRecurring) {
            AlarmDBHelper dbHelper = new AlarmDBHelper(getApplicationContext());
            dbHelper.updateAlarmStatus(alarmId, false);
        }

        Button buttonSnooze = findViewById(R.id.buttonSnooze);
        Button buttonSolve = findViewById(R.id.buttonSolve);
        buttonSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmRingActivity.this, SolveChallengeActivity.class);
                intent.putExtra("ALARM", alarm);
                intent.putExtra("TITLE", title);
                intent.putExtra("ALARM_ID", alarmId);
                intent.putExtra("CHALLENGE", challengeType);
                intent.putExtra("RECURRING", isRecurring);
                startActivity(intent);
                finish();
            }
        });
        buttonSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmDBHelper dbHelper = new AlarmDBHelper(getApplicationContext());
                dbHelper.updateAlarmStatus(alarmId, true);

                Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
                intent.putExtra("ALARM", alarm);
                intent.putExtra("TITLE", title);
                intent.putExtra("ALARM_ID", alarmId);
                intent.putExtra("CHALLENGE", challengeType);
                intent.putExtra("RECURRING", isRecurring);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmRingActivity.this,
                        alarmId + 20000, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 1); // add 1 minute for now
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                finish();
            }
        });
    }
}