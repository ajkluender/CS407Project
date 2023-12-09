package com.cs407.cs407finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cs407.cs407finalproject.data.Alarm;
import com.cs407.cs407finalproject.data.AlarmDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Alarm> alarmsList;
    private ArrayAdapter<String> adapter;
    private AlarmDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.alarmList);
        alarmsList = new ArrayList<>();
        dbHelper = new AlarmDBHelper(MainActivity.this);

        setupListView();
        setupAddAlarmButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAlarmList();
    }

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

    private String formatAlarmForDisplay(Alarm alarm) {
        return alarm.getTitle() + " - " + alarm.getHour() + ":"
                + String.format("%02d", alarm.getMinute());
    }

    private void sortAlarms() {
        Collections.sort(alarmsList, (a1, a2) -> {
            int hourCompare = Integer.compare(a1.getHour(), a2.getHour());
            if (hourCompare == 0) {
                return Integer.compare(a1.getMinute(), a2.getMinute());
            }
            return hourCompare;
        });
    }
}