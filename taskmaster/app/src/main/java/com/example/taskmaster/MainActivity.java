package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView username;
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

        username = findViewById(R.id.username);
        Button btnTask1 = findViewById(R.id.btn_task1);
        btnTask1.setOnClickListener(view -> {
            Intent intent = new Intent(this,DetailTask.class);
            intent.putExtra("title",btnTask1.getText());
            startActivity(intent);
        });
        Button btnTask2 = findViewById(R.id.btn_task2);
        btnTask2.setOnClickListener(view -> {
            Intent intent = new Intent(this,DetailTask.class);
            intent.putExtra("title",btnTask2.getText());
            startActivity(intent);
        });
        Button btnTask3 = findViewById(R.id.btn_task3);
        btnTask3.setOnClickListener(view -> {
            Intent intent = new Intent(this,DetailTask.class);
            intent.putExtra("title",btnTask3.getText());
            startActivity(intent);
        });


//        // Add Task Button
//        Button addButton = findViewById(R.id.addButton);
//        addButton.setOnClickListener(addButtonListener);
//
//        // ALL Task Button
//        Button allButton = findViewById(R.id.allButton);
//        allButton.setOnClickListener(allButtonListener);
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

    // Get the UserName
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: called - The App is VISIBLE");
        super.onResume();
        setUserName();
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

    // Option Menu  /////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                navigateToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void navigateToSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
/////////////////////////////////////////////////////////////////

    private void setUserName() {
        // get text out of shared preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // set text on text view User Name
        username.setText(sharedPreferences.getString(SettingsActivity.USERNAME, "No User Name setting"));
    }

}