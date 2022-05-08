package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmaster.data.Task;

import java.util.ArrayList;
import java.util.List;

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

    List<Task> tasksList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
//////////// Lab 28 Recycler View //////////////////////
        initialiseData();

        // get the recycler view object
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // create an Adapter // Custom Adapter
        CustomAdapter customRecyclerViewAdapter = new CustomAdapter(
                tasksList, position -> {
            Toast.makeText(
                    MainActivity.this,
                    "The task clicked => " + tasksList.get(position).getTitle(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), DetailTask.class);
            intent.putExtra("title",tasksList.get(position).getTitle());
            intent.putExtra("body",tasksList.get(position).getBody());
            intent.putExtra("state",tasksList.get(position).getState().toString());

            startActivity(intent);
        });
        // set adapter on recycler view
        recyclerView.setAdapter(customRecyclerViewAdapter);

        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

/////////// Lab 27 Intent ////////////////////

//        Button btnTask1 = findViewById(R.id.btn_task1);
//        btnTask1.setOnClickListener(view -> {
//            Intent intent = new Intent(this,DetailTask.class);
//            intent.putExtra("title",btnTask1.getText());
//            startActivity(intent);
//        });
//        Button btnTask2 = findViewById(R.id.btn_task2);
//        btnTask2.setOnClickListener(view -> {
//            Intent intent = new Intent(this,DetailTask.class);
//            intent.putExtra("title",btnTask2.getText());
//            startActivity(intent);
//        });
//        Button btnTask3 = findViewById(R.id.btn_task3);
//        btnTask3.setOnClickListener(view -> {
//            Intent intent = new Intent(this,DetailTask.class);
//            intent.putExtra("title",btnTask3.getText());
//            startActivity(intent);
//        });

/////////// Lab 26
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

/////////////////////////////////////////////////////////////////
    private  void initialiseData(){
        tasksList.add(new Task("Task1",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                Task.State.Complete));
        tasksList.add(new Task("Task2",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                Task.State.New));
        tasksList.add(new Task("Task3",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                Task.State.In_Progress));
    }
}