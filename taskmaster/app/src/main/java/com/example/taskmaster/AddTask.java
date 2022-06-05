package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;


public class AddTask extends AppCompatActivity implements OnMapReadyCallback  {
    private static final String TAG = "AddTask";
    private static final int REQUEST_CODE = 1234;
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    private final String[] state_Array = new String[]{"New", "In_Progress", "Complete"};
    File file = null;

    String imageKey = null;

    Spinner stateSelector;
    Spinner stateSelectorTeam;

    Button imageUploadButton;
    Button addButton;

    // initializing
    // FusedLocationProviderClient
    // object
    private FusedLocationProviderClient mFusedLocationClient;

    private final int PERMISSION_ID = 44;

    private double latitude;
    private double longitude;
    // in requestNewLocationData()
    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude=mLastLocation.getLongitude();
        }
    };


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
            if (file != null) {
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
                                                            .latitude(latitude)
                                                            .longitude(longitude)
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

            }else {
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
                                            .latitude(latitude)
                                            .longitude(longitude)
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
            }
            Toast.makeText(getApplicationContext(), "task Added", Toast.LENGTH_SHORT).show();
            addButton.setBackgroundColor(Color.RED);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get the location
        getLastLocation();

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


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                        }
                    }

                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }

    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat
                        .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}

