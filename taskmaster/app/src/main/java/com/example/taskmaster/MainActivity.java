package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // Move to the ADD TASK Page
    private final View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // Intent For navigation
            Intent addTaskActivity = new Intent(getApplicationContext(),AddTask.class);
            // Start Activity
            startActivity(addTaskActivity);
        }
    };
    // Move to the ALL TASK Page
    private final View.OnClickListener allButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // Intent For navigation
            Intent allTaskActivity = new Intent(getApplicationContext(),AllTask.class);
            // Start Activity
            startActivity(allTaskActivity);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add Task Button
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(addButtonListener);

        // ALL Task Button
        Button allButton = findViewById(R.id.allButton);
        allButton.setOnClickListener(allButtonListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: called");
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: called - The App is VISIBLE");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: called");
        super.onDestroy();
    }

}