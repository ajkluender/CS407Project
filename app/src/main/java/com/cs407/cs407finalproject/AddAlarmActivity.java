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
        dbHelper = new AlarmDBHelper(AddAlarmActivity.this);

        if (getIntent().hasExtra("ALARM_ID")) {
            int alarmId = getIntent().getIntExtra("ALARM_ID", -1);
            if (alarmId != -1) {
                isEditing = true;
                currentAlarm = dbHelper.getAlarmById(alarmId); // Implement this method in DBHelper
                if (currentAlarm != null) {
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

        FragmentContainerView fragmentContainerView = findViewById(R.id.fragmentContainerView);
        FragmentManager fragmentManager = getSupportFragmentManager();
        CheckBox addChallenge = findViewById(R.id.addChallenge);
        addChallenge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fragmentContainerView.setVisibility(View.VISIBLE);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, AddChallengeFragment.class, null)
                            .setReorderingAllowed(true)
                            .commit();
                } else {
                    fragmentContainerView.setVisibility(View.GONE);
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
                String challengeType = "none"; //replace this when we integrate challenges


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
                    dbHelper.deleteAlarm(currentAlarm.getAlarmId());
                }
                Toast.makeText(AddAlarmActivity.this, "Alarm Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void populateUIWithAlarmData(Alarm alarm) {
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
        FragmentContainerView fragmentContainerView = findViewById(R.id.fragmentContainerView);

    }
}