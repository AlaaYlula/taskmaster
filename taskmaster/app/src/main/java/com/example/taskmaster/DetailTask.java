package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.example.taskmaster.data.Task;
import com.example.taskmaster.database.AppDatabase;

public class DetailTask extends AppCompatActivity {

    public static final String TAG = DetailTask.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        // Get Intent
        Intent passedIntent = getIntent();

        // get the data
        // Read https://stackoverflow.com/questions/7074097/how-to-pass-integer-from-one-activity-to-another
       // int idRecived = passedIntent.getIntExtra("id",0);

        String id = passedIntent.getStringExtra("id");
        //get the task by Id
        //Task taskRecived = AppDatabase.getInstance(getApplicationContext()).taskDao().getTaskById(idRecived);



        TextView title = findViewById(R.id.title);
        TextView body = findViewById(R.id.description);
        TextView state = findViewById(R.id.state);

//        title.setText(taskRecived.getTitle());
//        body.setText(taskRecived.getBody());
//        state.setText(taskRecived.getState().toString());

        // API query
        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class, com.amplifyframework.datastore.generated.model.Task.ID.eq(id)),
                response -> {
                    for (com.amplifyframework.datastore.generated.model.Task task : response.getData()) {
                        Log.i(TAG, "------------------> " + task.getTitle());
                        title.setText(task.getTitle());
                        body.setText(task.getDescription());
                        state.setText(task.getState().toString());
                    }
                },
                error -> Log.e(TAG, "Query failure", error)
        );

        //Back Button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            // Create Intent
            Intent startMainTaskActivity = new Intent(getApplicationContext(),MainActivity.class);
            //start
            startActivity(startMainTaskActivity);
        });
        //Delete Button
        Button deleteButton = findViewById(R.id.deleteBtn);
        deleteButton.setOnClickListener(view -> {
          // AppDatabase.getInstance(getApplicationContext()).taskDao().deleteTask(taskRecived);
            // Create Intent
            Intent startMainTaskActivity = new Intent(getApplicationContext(),MainActivity.class);
            //start
            startActivity(startMainTaskActivity);
        });
    }
}