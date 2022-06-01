package com.example.taskmaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import android.net.Uri;

public class AddTask extends AppCompatActivity {
    private static final String TAG = "AddTask";
    private static final int REQUEST_CODE = 1234;
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    private String[] state_Array = new String[]{"New", "In_Progress", "Complete"};
    File file = null;

    String imageKey = null;

    Spinner stateSelector;
    Spinner stateSelectorTeam;

    Button imageUploadButton;
    Button addButton;
    // Add Task
    private final View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            EditText titleEdit = findViewById(R.id.username);
            EditText descriptionEdit = findViewById(R.id.doTask);
            Spinner stateSelector = findViewById(R.id.state_selector);
            Spinner teamSelector = findViewById(R.id.teams);

            //convert to string
            String title = titleEdit.getText().toString();
            String description = descriptionEdit.getText().toString();
            // Read https://stackoverflow.com/questions/1947933/how-to-get-spinner-value
            String state_String = stateSelector.getSelectedItem().toString();
            String team_String = teamSelector.getSelectedItem().toString();

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

            // upload to s3
            // uploads the Image
            Amplify.Storage.uploadFile(
                    title + ".jpg",
                    file, // From upload picker Image OR share Image
                    result -> {
                        Log.i(TAG, "Successfully uploaded: " + result.getKey());
                        runOnUiThread(() -> {
                            imageKey = result.getKey();
                            // Lab 32  // Add the Task to the DynamoDB
                            Amplify.API.query(
                                    ModelQuery.list(Team.class, Team.NAME.eq(team_String)),
                                    response -> {
                                        for (Team teamLoop : response.getData()) {
                                            if (teamLoop.getName().equals(team_String)) {
                                                Task task = Task.builder()
                                                        .title(title)
                                                        .teamTasksId(teamLoop.getId())
                                                        .description(description)
                                                        .state(Enum.valueOf(State.class, state_String))
                                                        .image(imageKey)
                                                        .build();
                                                Log.i(TAG, "***** Saved item: " + task);
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
                                            }
                                        }

                                    },
                                    error -> Log.e(TAG, error.toString(), error)
                            );
                        });
                    },
                    storageFailure -> Log.e(TAG, "Upload failed", storageFailure)

            );

            Toast.makeText(getApplicationContext(), "task Added", Toast.LENGTH_SHORT).show();
            addButton.setBackgroundColor(Color.RED);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

//        // https://developer.android.com/training/basics/intents/filters
//        // Get the intent that started this activity
//        Intent intentReceive = getIntent();
//        Uri data = intentReceive.getData();
//
//        // Figure out what to do based on the intent type
//        if (intentReceive.getType().indexOf("image/") != -1) {
//            // Handle intents with image data ...
//            Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
//        } else if (intentReceive.getType().equals("text/plain")) {
//            // Handle intents with text ...
//        }

        // Get intent, action and MIME type
        Intent intentReceive = getIntent();
        String action = intentReceive.getAction();
        String type = intentReceive.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                Log.i(TAG, "handleSendText: Type => " + type);
                handleMainFunctionallity();
                handleSendText(intentReceive); // Handle text being sent
            } else if (type.startsWith("image/")) {
                Log.i(TAG, "handleSendImage: Type => " + type);
                handleMainFunctionallity();
                handleSendImage(intentReceive); // Handle single image being sent

            }
        } else { // Handle other intents, such as being started from the home screen
            handleMainFunctionallity();
        }

    }

    private void handleMainFunctionallity() {
        // Add Task Button
        addButton = findViewById(R.id.button);
        addButton.setOnClickListener(addButtonListener);

        ////////////////////// create adapter for the State Spinner
        setStateSpinner();
        ////////////////////// Team Spinner
        setTeamSpinner();
        ////////////////////////////
        // Lab 37
        // Image Upload
        imageUploadButton = findViewById(R.id.image_upload);
        imageUploadButton.setOnClickListener(view2 -> {
            // Launches photo picker in single-select mode.
            // This means that the user can select one photo or video.
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            startActivityForResult(intent, REQUEST_CODE);
        });

        //Back Button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            // Create Intent
            Intent startMainTaskActivity = new Intent(getApplicationContext(),MainActivity.class);
            //start
            startActivity(startMainTaskActivity);
        });
    }

    private void setStateSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                state_Array
        );

        stateSelector = findViewById(R.id.state_selector);

        // set adapter
        stateSelector.setAdapter(spinnerAdapter);
    }

    private void setTeamSpinner() {

        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    ArrayList<Team> teamsList = new ArrayList<>();
                    for (Team team : response.getData()) {
                        teamsList.add(team);
                    }

                    runOnUiThread(() -> {
                        String[] teamsName = new String[teamsList.size()];

                        for (int i = 0; i < teamsList.size(); i++) {
                            teamsName[i] = teamsList.get(i).getName();
                        }
                        // create adapter
                        ArrayAdapter<String> spinnerAdapterTeam = new ArrayAdapter<String>(
                                this,
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                teamsName
                        );
                        stateSelectorTeam = findViewById(R.id.teams);

                        // set adapter
                        stateSelectorTeam.setAdapter(spinnerAdapterTeam);
                    });
                },
                error -> Log.e(TAG, "Query failure", error)
        );

    }

    // Lab 37 // https://github.com/LTUC/amman-401d6-java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            Log.e(TAG, "onActivityResult: Error getting image from device");
            return;
        }

        switch(requestCode) {
            case REQUEST_CODE:
                // Get photo picker response for single select.
                Uri currentUri = data.getData();

                    // Do stuff with the photo/video URI.
                    Log.i(TAG, "onActivityResult: the uri is => " + currentUri);
                    convertUriToFile(currentUri);
                    imageUploadButton.setText("Image uploaded");
                    imageUploadButton.setBackgroundColor(Color.RED);
                return;
        }
    }

    /*
       https://stackoverflow.com/questions/2169649/get-pick-an-image-from-androids-built-in-gallery-app-programmatically
        */
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        return image;
    }

    private void convertUriToFile(Uri currentUri) {
        try {
            Bitmap bitmap = getBitmapFromUri(currentUri);

            // Get the title of the task
            EditText titleEdit = findViewById(R.id.username);
            String title = titleEdit.getText().toString();

            file = new File(getApplicationContext().getFilesDir(), title+".jpg");
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.i(TAG, "handleSendText: the text is => " + sharedText);
        if (sharedText != null) {

            // Update UI to reflect text being shared
            Log.i(TAG, "handleSendText: the text is => " + sharedText);
            Uri imageUri = Uri.parse(sharedText);
            Log.i(TAG, "handleSendText: the Uri is => " + imageUri);
           // convertUriToFile(imageUri);
            getBitmapFromURL(sharedText);

        }
        ///////////////////////////

    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared

            // Do stuff with the photo/video URI.
            Log.i(TAG, "handleSendImage: the uri is => " + imageUri);
            convertUriToFile(imageUri);
            imageUploadButton.setText("Image uploaded");
            imageUploadButton.setBackgroundColor(Color.RED);
        }
    }

    // http://www.java2s.com/example/android/graphics/download-image-from-url.html ///////////////

    //https://mkyong.com/java/how-to-convert-inputstream-to-file-in-java/ //////////////////////////

    // https://stackoverflow.com/questions/11831188/how-to-get-bitmap-from-a-url-in-android ////////////////////////
    public void getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            // Get the title of the task
            EditText titleEdit = findViewById(R.id.username);
            String title = titleEdit.getText().toString();

            file = new File(getApplicationContext().getFilesDir(), title+".jpg");
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // Author: silentnuke

}