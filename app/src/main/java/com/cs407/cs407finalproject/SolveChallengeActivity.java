package com.cs407.cs407finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class SolveChallengeActivity extends AppCompatActivity {

    int challengeType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_challenge);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CHALLENGE")) {
            challengeType = intent.getIntExtra("CHALLENGE", 0);
        }

        TextView challengeTitle = findViewById(R.id.challengeTitle);
        TextView challengeDescription = findViewById(R.id.challengeDescription);
        TextView challengeContent = findViewById(R.id.challengeContent);
        EditText userAnswer = findViewById(R.id.userAnswer);

        Random rand = new Random();
        int num1 = rand.nextInt(99) + 1;
        int num2 = rand.nextInt(99) + 1;
        int num3 = rand.nextInt(9) + 1;
        int num4 = rand.nextInt(9) + 1;
        int num5 = rand.nextInt(9999999) + 1;
        int num6 = rand.nextInt(30) + 1;
        int challengeSolution;

        if (challengeType == 0) { // no challenge
            challengeTitle.setText("");
            challengeDescription.setText("");
            challengeContent.setText("");
            challengeSolution = -1;
            startActivity(new Intent(SolveChallengeActivity.this, MainActivity.class));
            finish();
        } else if (challengeType == 1) { // addition
            challengeTitle.setText("ADDITION");
            challengeDescription.setText("Solve the following:");
            challengeContent.setText(num1 + " + " + num2);
            challengeSolution = num1 + num2;
        } else if (challengeType == 2) { // multiplication
            challengeTitle.setText("MULTIPLICATION");
            challengeDescription.setText("Solve the following:");
            challengeContent.setText(num3 + " * " + num4);
            challengeSolution = num3 * num4;
        } else if (challengeType == 3) { // sequence
            challengeTitle.setText("SEQUENCE");
            challengeDescription.setText("Copy the following:");
            challengeContent.setText(num5);
            challengeSolution = num5;
        } else if (challengeType == 4) { // square
            challengeTitle.setText("SQUARE");
            challengeDescription.setText("Square the following:");
            challengeContent.setText("" + num6);
            challengeSolution = num6 * num6;
        } else { // no challenge
            challengeTitle.setText("");
            challengeDescription.setText("");
            challengeContent.setText("");
            challengeSolution = -1;
            startActivity(new Intent(SolveChallengeActivity.this, MainActivity.class));
            finish();
        }

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(userAnswer.getText().toString()) == challengeSolution) {
                    Intent intent = new Intent(SolveChallengeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    submitButton.setText("Try Again");
                }
            }
        });
    }
}