package com.cs407.cs407finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cs407.cs407finalproject.data.Alarm;
import com.cs407.cs407finalproject.data.AlarmDBHelper;

public class AddAlarmActivity extends AppCompatActivity {

    private Alarm currentAlarm;
    private boolean isEditing = false;
    private AlarmDBHelper dbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        // Initialize the database helper
        dbHelper = new AlarmDBHelper(AddAlarmActivity.this);

        if (getIntent().hasExtra("ALARM_ID")) {
            int alarmId = getIntent().getIntExtra("ALARM_ID", -1);
            if (alarmId != -1) {
                isEditing = true;
                // Retrieve the alarm to be edited from the database
                currentAlarm = dbHelper.getAlarmById(alarmId); // Implement this method in DBHelper
                if (currentAlarm != null) {
                    // Populate the UI with the alarm data
                    populateUIWithAlarmData(currentAlarm);
                }
            }
        }

        LinearLayout recurringDates = findViewById(R.id.recurringDates);
        CheckBox recurringAlarm = findViewById(R.id.recurringAlarm);
        recurringAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recurringDates.setVisibility(View.VISIBLE);
                } else {
                    recurringDates.setVisibility(View.GONE);
                }
            }
        });

        RadioGroup allChallenges = findViewById(R.id.allChallenges);
        CheckBox addChallenge = findViewById(R.id.addChallenge);
        addChallenge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    allChallenges.setVisibility(View.VISIBLE);
                } else {
                    allChallenges.setVisibility(View.GONE);
                }
            }
        });

        Button saveAlarm = findViewById(R.id.saveAlarm);
        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker timePicker = findViewById(R.id.timePickerUser);
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                EditText alarmNameEditText = findViewById(R.id.alarmName);
                String title = alarmNameEditText.getText().toString();

                boolean isOn = true; // A new alarm is on by default
                boolean isRecurring = recurringAlarm.isChecked();
                boolean monday = ((CheckBox) findViewById(R.id.checkMon)).isChecked();
                boolean tuesday = ((CheckBox) findViewById(R.id.checkTue)).isChecked();
                boolean wednesday = ((CheckBox) findViewById(R.id.checkWed)).isChecked();
                boolean thursday = ((CheckBox) findViewById(R.id.checkThu)).isChecked();
                boolean friday = ((CheckBox) findViewById(R.id.checkFri)).isChecked();
                boolean saturday = ((CheckBox) findViewById(R.id.checkSat)).isChecked();
                boolean sunday = ((CheckBox) findViewById(R.id.checkSun)).isChecked();

                int challengeType = -1;
                CheckBox addChallenge = findViewById(R.id.addChallenge);
                RadioButton button1 = findViewById(R.id.button1);
                RadioButton button2 = findViewById(R.id.button2);
                RadioButton button3 = findViewById(R.id.button3);
                RadioButton button4 = findViewById(R.id.button4);
                if (addChallenge.isChecked()) {
                    if (button1.isChecked()) {
                        challengeType = 1;
                    } else if (button2.isChecked()) {
                        challengeType = 2;
                    } else if (button3.isChecked()) {
                        challengeType = 3;
                    } else if (button4.isChecked()) {
                        challengeType = 4;
                    }
                } else {
                    challengeType = -1;
                }

                int alarmId = isEditing ? currentAlarm.getAlarmId() : 0;
                Alarm newAlarm = new Alarm(alarmId, hour, minute, title, challengeType, isOn, isRecurring,
                        monday, tuesday, wednesday,thursday, friday,saturday, sunday);

                AlarmDBHelper dbHelper = new AlarmDBHelper(AddAlarmActivity.this);

                long id;
                if (isEditing) {
                    id = dbHelper.updateAlarm(newAlarm);
                } else {
                    id = dbHelper.insertAlarm(newAlarm);
                }

                if (id != -1) {
                    Toast.makeText(AddAlarmActivity.this, "Alarm Saved!", Toast.LENGTH_SHORT).show();
                    Alarm.scheduleAlarm(getApplicationContext(), newAlarm); // Schedule the alarm
                } else {
                    Toast.makeText(AddAlarmActivity.this, "Error Saving Alarm", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        Button deleteAlarm = findViewById(R.id.deleteAlarm);
        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAlarm != null) {
                    Alarm.cancelAlarm(getApplicationContext(), currentAlarm);
                    dbHelper.deleteAlarm(currentAlarm.getAlarmId());
                }
                Toast.makeText(AddAlarmActivity.this, "Alarm Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateUIWithAlarmData(Alarm alarm) {
        // Populate the UI fields with data from the alarm object
        TimePicker timePicker = findViewById(R.id.timePickerUser);
        timePicker.setHour(alarm.getHour());
        timePicker.setMinute(alarm.getMinute());

        EditText alarmNameEditText = findViewById(R.id.alarmName);
        alarmNameEditText.setText(alarm.getTitle());

        CheckBox recurringAlarm = findViewById(R.id.recurringAlarm);
        recurringAlarm.setChecked(alarm.isRecurring());

        LinearLayout recurringDates = findViewById(R.id.recurringDates);
        recurringDates.setVisibility(alarm.isRecurring() ? View.VISIBLE : View.GONE);

        CheckBox monday = findViewById(R.id.checkMon);
        monday.setChecked(alarm.isMonday());

        CheckBox tuesday = findViewById(R.id.checkTue);
        tuesday.setChecked(alarm.isTuesday());

        CheckBox wednesday = findViewById(R.id.checkWed);
        wednesday.setChecked(alarm.isWednesday());

        CheckBox thursday = findViewById(R.id.checkThu);
        thursday.setChecked(alarm.isThursday());

        CheckBox friday = findViewById(R.id.checkFri);
        friday.setChecked(alarm.isFriday());

        CheckBox saturday = findViewById(R.id.checkSat);
        saturday.setChecked(alarm.isSaturday());

        CheckBox sunday = findViewById(R.id.checkSun);
        sunday.setChecked(alarm.isSunday());

        CheckBox addChallenge = findViewById(R.id.addChallenge);
        addChallenge.setChecked(alarm.getChallengeType() != -1);

        RadioGroup allChallenges = findViewById(R.id.allChallenges);
        allChallenges.setVisibility((alarm.getChallengeType() != -1) ? View.VISIBLE : View.GONE);

        RadioButton button1 = findViewById(R.id.button1);
        button1.setChecked(alarm.getChallengeType() == 1);

        RadioButton button2 = findViewById(R.id.button2);
        button2.setChecked(alarm.getChallengeType() == 2);

        RadioButton button3 = findViewById(R.id.button3);
        button3.setChecked(alarm.getChallengeType() == 3);

        RadioButton button4 = findViewById(R.id.button4);
        button4.setChecked(alarm.getChallengeType() == 4);
    }
}