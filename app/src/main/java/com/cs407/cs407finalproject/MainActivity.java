package com.cs407.cs407finalproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cs407.cs407finalproject.data.Alarm;
import com.cs407.cs407finalproject.data.AlarmDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MainActivity the first activity that the user will see, and manages the app.
 * Extends AppCompactActivity by default
 */
public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Alarm> alarmsList;
    private ArrayAdapter<String> adapter;
    private AlarmDBHelper dbHelper;

    /**
     * OnCreate method, imported from AppCompactActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.alarmList);
        alarmsList = new ArrayList<>();
        dbHelper = new AlarmDBHelper(MainActivity.this);

        setupListView();
        setupAddAlarmButton();
        requestPermission();
    }

    /**
     * Resumes the application
     */
    @Override
    protected void onResume() {
        super.onResume();
        refreshAlarmList();
    }

    /**
     * Sets up the list view for the alarms
     */
    private void setupListView() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AddAlarmActivity.class);
                Alarm selectedAlarm = alarmsList.get(position);
                intent.putExtra("ALARM_ID", selectedAlarm.getAlarmId());
                startActivity(intent);
            }
        });
    }

    /**
     * Sets up the button to add a new button
     */
    private void setupAddAlarmButton() {
        Button addAlarm = findViewById(R.id.addAlarm);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAlarmActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     *
     */
    private void refreshAlarmList() {
        alarmsList.clear();
        alarmsList.addAll(dbHelper.getAllAlarms());
        sortAlarms();

        List<String> displayList = new ArrayList<>();
        for (Alarm alarm : alarmsList) {
            String displayText = formatAlarmForDisplay(alarm);
            displayList.add(displayText);
        }
        adapter.clear();
        adapter.addAll(displayList);
        adapter.notifyDataSetChanged();
    }

    /**
     *
     * @param alarm
     * @return
     */
    private String formatAlarmForDisplay(Alarm alarm) {
        String title = "untitled";
        if (alarm.getTitle().length() > 0) {
            title = alarm.getTitle();
        }
        if (alarm.isOn()) {
            return "[ON] " + alarm.getHour() + ":" + String.format("%02d", alarm.getMinute())
                    + " - " + title;
        }
        return "[OFF] " + alarm.getHour() + ":" + String.format("%02d", alarm.getMinute())
                + " - " + title;
    }

    /**
     *
     */
    private void sortAlarms() {
        Collections.sort(alarmsList, (a1, a2) -> {
            int hourCompare = Integer.compare(a1.getHour(), a2.getHour());
            if (hourCompare == 0) {
                return Integer.compare(a1.getMinute(), a2.getMinute());
            }
            return hourCompare;
        });
    }

    /**
     *
     */
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(!isGranted) {
                    Toast.makeText(this, "Enable notifications to continue.", Toast.LENGTH_LONG).show();
                }
            });

    /**
     *
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return;
        }
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }
}