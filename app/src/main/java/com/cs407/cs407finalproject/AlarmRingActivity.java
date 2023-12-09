package com.cs407.cs407finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmRingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CHALLENGE")) {
            int challengeType = intent.getIntExtra("CHALLENGE", 0);
        }

        FragmentContainerView fragmentContainerView = findViewById(R.id.fragmentContainerView);
        TextView greeting = findViewById(R.id.greeting);
        Button buttonSnooze = findViewById(R.id.buttonSnooze);
        Button buttonSolve = findViewById(R.id.buttonSolve);
        buttonSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentContainerView.setVisibility(View.VISIBLE);
                greeting.setVisibility(View.GONE);
                buttonSnooze.setVisibility(View.GONE);
                buttonSolve.setVisibility(View.GONE);
            }
        });
    }
}