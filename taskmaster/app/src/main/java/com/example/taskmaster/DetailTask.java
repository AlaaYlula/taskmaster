package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        // Get Intent
        Intent passedIntent = getIntent();

        // get the data
        String titleRecived = passedIntent.getStringExtra("title");
        String bodyRecived = passedIntent.getStringExtra("body");
        String stateRecived = passedIntent.getStringExtra("state");

        TextView title = findViewById(R.id.title);
        TextView body = findViewById(R.id.description);
        TextView state = findViewById(R.id.state);

        title.setText(titleRecived);
        body.setText(bodyRecived);
        state.setText(stateRecived);


    }
}