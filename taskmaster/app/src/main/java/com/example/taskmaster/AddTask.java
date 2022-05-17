package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;

import java.nio.charset.StandardCharsets;


// import com.example.taskmaster.data.Task;

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

            // To convert the String to Enum so can added to the Task constructor
            //Task.State state = Enum.valueOf(Task.State.class,state_String);

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

//            // Lab 28
//            Task task = new Task(title,description,state);
//            Long newTaskId = AppDatabase.getInstance(getApplicationContext()).taskDao().insertTask(task);
//            //System.out.println("***************************"+newTaskId);

            // Lab 32
            Task task = Task.builder()
                    .title(title)
                    .description(description)
                    .state(Enum.valueOf(State.class,state_String))
                    .build();

            Log.i(TAG, "***** Saved item: " +task);
            // Data store save
            Amplify.DataStore.save(task,
                    success -> Log.i(TAG, "Saved item: " + success.item().getTitle()),
                    error -> Log.e(TAG, "Could not save item to DataStore", error)
            );

            // API save to backend
            Amplify.API.mutate(
                    ModelMutation.create(task),
                   success -> Log.i(TAG, "Saved item: " + success.getData().getTitle()),
                   error -> Log.e(TAG, "Could not save item to API", error)
            );

            // Datastore and API sync
            Amplify.DataStore.observe(Task.class,
                    started -> {
                        Log.i(TAG, "Observation began.");
                        // TODO: 5/17/22 Update the UI thread with in this call method
                        // Manipulate your views

                        // call handler
                    },
                    change -> Log.i(TAG, change.item().toString()),
                    failure -> Log.e(TAG, "Observation failed.", failure),
                    () -> Log.i(TAG, "Observation complete.")
            );

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