package com.cs407.cs407finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmRingActivity extends AppCompatActivity {

    int challengeType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CHALLENGE")) {
            challengeType = intent.getIntExtra("CHALLENGE", 0);
        }

        Button buttonSnooze = findViewById(R.id.buttonSnooze);
        Button buttonSolve = findViewById(R.id.buttonSolve);
        buttonSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmRingActivity.this, SolveChallengeActivity.class);
                intent.putExtra("CHALLENGE", challengeType);
                startActivity(intent);
                finish();
            }
        });
        buttonSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delay alarm by 5-10 minutes
            }
        });
    }
}