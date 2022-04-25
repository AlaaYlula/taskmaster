package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {
    private static final String TAG = "AddTask";

    // Add Task
    private final View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            EditText titleEdit = findViewById(R.id.myTask);
            EditText descriptionEdit = findViewById(R.id.doTask);
            //convert to string
            String title = titleEdit.getText().toString();
            String description = descriptionEdit.getText().toString();

            // Create Intent
            Intent startAllTaskActivity = new Intent(getApplicationContext(),AllTask.class);

            // add data
            startAllTaskActivity.putExtra("title",title);
            startAllTaskActivity.putExtra("description",description);

            //start
            startActivity(startAllTaskActivity);

            Toast.makeText(getApplicationContext(), "task Added", Toast.LENGTH_SHORT).show();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        // Add Task Button
        Button addButton = findViewById(R.id.button);
        addButton.setOnClickListener(addButtonListener);

    }
}