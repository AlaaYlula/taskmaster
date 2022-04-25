package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AllTask extends AppCompatActivity {

    private final View.OnClickListener backButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Create Intent
            Intent startMainTaskActivity = new Intent(getApplicationContext(),MainActivity.class);
            //start
            startActivity(startMainTaskActivity);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_task);

        // Get Intent
        Intent passedIntent = getIntent();

        // get the data
        String title = passedIntent.getStringExtra("title");
        String description = passedIntent.getStringExtra("description");

        // Set the text view
        TextView titleText = findViewById(R.id.textTitle);
        TextView descText = findViewById(R.id.textDesc);

        titleText.setText(title);
        descText.setText(description);

        //Button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(backButtonListener);

    }
}