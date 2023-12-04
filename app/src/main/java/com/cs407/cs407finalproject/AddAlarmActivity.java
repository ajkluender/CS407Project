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
import android.widget.LinearLayout;

public class AddAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

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
                Intent intent = new Intent(AddAlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}