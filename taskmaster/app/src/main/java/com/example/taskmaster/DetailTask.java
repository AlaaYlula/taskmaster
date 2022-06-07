package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DetailTask extends AppCompatActivity {

    public static final String TAG = DetailTask.class.toString();
    private Handler handler;
    TextView title;
    TextView body ;
    TextView state ;
    TextView latitude ;
    TextView longitude ;
    ImageView imageView ;

    Bitmap bitmap = null;

    Task task;

    private final MediaPlayer mp = new MediaPlayer();

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

        title = findViewById(R.id.title);
        body = findViewById(R.id.description);
        state = findViewById(R.id.state);
        imageView = findViewById(R.id.image);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        // API query
//        Amplify.API.query(
//                ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class, com.amplifyframework.datastore.generated.model.Task.ID.eq(id)),
//                response -> {
//                    for (com.amplifyframework.datastore.generated.model.Task task : response.getData()) {
//                        Log.i(TAG, "------------------> " + task.getTitle());
//                        taskReceived.set(task);
//                    }
//                    runOnUiThread(() -> {
//                    });
//                },
//                error -> Log.e(TAG, "Query failure", error)
//        );

        Amplify.API.query(
                ModelQuery.get(Task.class, id),
                response -> {
                    Task taskTest = response.getData();
                    task = taskTest;
                    if(taskTest.getImage() != null) {
                       setImage(taskTest.getImage(), taskTest);
                      // new ImageDownloader().execute(taskTest.getImage());
                    }
                        // Send message to the handler to show The task Details  >>
//                        Bundle bundle = new Bundle();
//                        bundle.putString("taskReceived", response.toString());
//                        bundle.putString("title", taskTest.getTitle());
//                        bundle.putString("body", taskTest.getDescription());
//                        bundle.putString("state", taskTest.getState().toString());
//
//                        Message message = new Message();
//                        message.setData(bundle);
//
//                        handler.sendMessage(message);

                    // Use To do Sync
                    runOnUiThread(() -> {
                        title.setText(taskTest.getTitle());
                        body.setText(taskTest.getDescription());
                        state.setText(taskTest.getState().toString());
                        if(!taskTest.getLatitude().toString().equals("") && !taskTest.getLongitude().toString().equals("")) {
                            latitude.setText(taskTest.getLatitude().toString());
                            longitude.setText(taskTest.getLongitude().toString());
                        }
                    });
                },
                error -> Log.e("MyAmplifyApp", error.toString(), error)
        );
        // Receive message from API Query to show the Task Details >>
        handler = new Handler(Looper.getMainLooper(), msg -> {

            title.setText(msg.getData().getString("title"));
            body.setText(msg.getData().getString("body"));
            state.setText(msg.getData().getString("state"));
            return true;

        });

        // Sound button
        Button soundBtn = findViewById(R.id.sound);
        soundBtn.setOnClickListener(view -> {
            Amplify.Predictions.convertTextToSpeech(
                    body.getText().toString(),
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e(TAG, "Conversion failed", error)
            );
        });

        // translate button
        Button translateBtn = findViewById(R.id.translate);
        translateBtn.setOnClickListener(view -> {
            Amplify.Predictions.translateText(body.getText().toString(),
                    result -> {
                Log.i(TAG, result.getTranslatedText());

                        runOnUiThread(() -> {
                            TextView translateResult = findViewById(R.id.translate_text);
                            translateResult.setText(result.getTranslatedText());
                        });
                    },
                    error -> Log.e(TAG, "Translation failed", error)
            );
        });
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

            // https://docs.amplify.aws/lib/restapi/delete/q/platform/android/
//            RestOptions options = RestOptions.builder()
//                    .addPath(task.getTitle())
//                    .build();
//
//            Amplify.API.delete(options,
//                    response2 -> Log.i(TAG, "DELETE succeeded: " + response2),
//                    error -> Log.e(TAG, "DELETE failed.", error)
//            );
            // Create Intent
            Intent startMainTaskActivity = new Intent(getApplicationContext(),MainActivity.class);
            //start
            startActivity(startMainTaskActivity);
        });
    }

    private void playAudio(InputStream audioData) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = audioData.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }

    private void setImage(String image, Task taskTest) {
        if(image != null) {
            Amplify.Storage.downloadFile(
                    image,
                    new File(getApplicationContext().getFilesDir() + "/" + image + "download.jpg"),
                    result -> {
                        Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                        Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());
                        runOnUiThread(() -> {

                            // Using Glide Library: https://github.com/bumptech/glide
                            Glide.with(this).load(result.getFile().getPath()).into(imageView);
                            // Using bitmap :
                            // bitmap = BitmapFactory.decodeFile(result.getFile().getPath());
                            // imageView.setImageBitmap(bitmap);
                        });
                    },
                    error -> Log.e(TAG, "Download Failure", error)
            );
        }
    }

    // https://www.youtube.com/watch?v=6FMqgAzKuOg
    private class ImageDownloader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            Amplify.Storage.downloadFile(
                    strings[0],
                    new File(getApplicationContext().getFilesDir() + "/" + strings[0] + "download.jpg"),
                    result -> {
                        Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                        Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());
                       bitmap = BitmapFactory.decodeFile(result.getFile().getPath());
                    },
                    error -> Log.e(TAG, "Download Failure", error)
            );
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}