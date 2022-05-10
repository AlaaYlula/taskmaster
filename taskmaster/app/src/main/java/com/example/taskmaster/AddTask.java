package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Entity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.taskmaster.data.Task;
import com.example.taskmaster.database.AppDatabase;

public class AddTask extends AppCompatActivity {
    private static final String TAG = "AddTask";
    private String[] state_Array = new String[]{"New", "In_Progress", "Complete"};
    // Add Task
    private final View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            EditText titleEdit = findViewById(R.id.username);
            EditText descriptionEdit = findViewById(R.id.doTask);
            Spinner stateSelector = findViewById(R.id.state_selector);

            //convert to string
            String title = titleEdit.getText().toString();
            String description = descriptionEdit.getText().toString();
            // Read https://stackoverflow.com/questions/1947933/how-to-get-spinner-value
            String state_String = stateSelector.getSelectedItem().toString();

            Task.State state = Enum.valueOf(Task.State.class,state_String);

            //Lab 26
//            // Create Intent
//            Intent startAllTaskActivity = new Intent(getApplicationContext(),AllTask.class);

//            // add data
//            startAllTaskActivity.putExtra("title",title);
//            startAllTaskActivity.putExtra("description",description);
//
//
//            //start
//            startActivity(startAllTaskActivity);

            // Lab 28
            Task task = new Task(title,description,state);
            Long newTaskId = AppDatabase.getInstance(getApplicationContext()).taskDao().insertTask(task);
            //System.out.println("***************************"+newTaskId);

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

        // create adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                state_Array
        );

        Spinner stateSelector = findViewById(R.id.state_selector);

        // set adapter
        stateSelector.setAdapter(spinnerAdapter);

        //Back Button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            // Create Intent
            Intent startMainTaskActivity = new Intent(getApplicationContext(),MainActivity.class);
            //start
            startActivity(startMainTaskActivity);
        });

    }
}