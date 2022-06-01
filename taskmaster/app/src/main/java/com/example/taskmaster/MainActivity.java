package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    CustomAdapter customRecyclerViewAdapter;
    TextView username;
    List<Team> teamsListData = new ArrayList<>();
    List<Task> tasksListDatabase = new ArrayList<>();

    String teamName="";
    private Handler handler;
    private Handler handler2;

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

    //List<com.example.taskmaster.data.Task> tasksList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //authSession("onCreate");
        authAttribute();
        // Read : https://medium.com/@dantasfiles/three-methods-to-get-user-information-in-aws-amplify-authentication-e4e39e658c33
       // Auth.currentUserInfo();

//        // Amplify Configure // Moved to taskMAsterApplication class
//        configureAmplify();

        username = findViewById(R.id.username);

        initialTeams();

//////////// Lab 28 Recycler View //////////////////////

        //initialiseData();

        //Lab 29
        // Get All Tasks Using Room :
        //List<Task> tasksListDatabase = AppDatabase.getInstance(getApplicationContext()).taskDao().getAll();

        // Get All Tasks Using Amplify Query
        getTeamTask();

        // Fetch the Data From the Database Locally
//        Amplify.DataStore.query(Task.class,
//                tasks -> {
//                    while (tasks.hasNext()) {
//                        Task task = tasks.next();
//                        tasksListDatabase.add(task);
//                    }
//                },
//                failure -> Log.e(TAG, "Could not query DataStore", failure)
//        );
        //////////////////////////////////////////////
        //Lab 32 :
        // Receive message from API Query to show the Tasks List in the Recycler View >>
        handler = new Handler(Looper.getMainLooper(), msg -> {
                // get the recycler view object
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                // create an Adapter // Custom Adapter
                customRecyclerViewAdapter = new CustomAdapter(
                        tasksListDatabase, position -> {
                    Toast.makeText(
                            MainActivity.this,
                            "The task clicked => " + tasksListDatabase.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), DetailTask.class);
                    //send the Id
                    intent.putExtra("id", tasksListDatabase.get(position).getId());
                    startActivity(intent);
                });
                // set adapter on recycler view
                recyclerView.setAdapter(customRecyclerViewAdapter);

                // set other important properties
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                return true;

        });

        // Lab 36
        // To set the User Name
        handler2 = new Handler(Looper.getMainLooper(), msg -> {
            // get the username
            //String user = Amplify.Auth.getCurrentUser().getUsername();
            String user = msg.getData().getString("name");
            username.setText(user);
            return true;

        });


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

    private void getTeamTask(){
        Log.i(TAG, "==== Team Inside ==== " + teamName);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                teams -> {
                    Log.i(TAG, "==== Teams List  ====" + teams);
                   // if (teams.hasData()) {
                        for (Team team : teams.getData()) {
                            if (team.getName().equals(teamName)) {
                                Amplify.API.query(
                                        ModelQuery.list(Task.class, Task.TEAM_TASKS_ID.eq(team.getId())),
                                        success -> {
                                            tasksListDatabase = new ArrayList<>();
                                            if (success.hasData()) {
                                                for (Task task : success.getData()) {
                                                    tasksListDatabase.add(task);
                                                }
                                            }
                                            Log.i(TAG, "==== Task List  ====" + tasksListDatabase);
                                            // Send message to the handler to show the Tasks List in the Recycler View >>
                                            Bundle bundle = new Bundle();
                                            bundle.putString("teamName", "teamName");

                                            Message message = new Message();
                                            message.setData(bundle);

                                            handler.sendMessage(message);
//                                        runOnUiThread(() -> {
//                                            // get the recycler view object
//                                            RecyclerView recyclerView = findViewById(R.id.recycler_view);
//                                            // create an Adapter // Custom Adapter
//                                            customRecyclerViewAdapter = new CustomAdapter(
//                                                    tasksListDatabase, position -> {
//                                                Toast.makeText(
//                                                        MainActivity.this,
//                                                        "The task clicked => " + tasksListDatabase.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//
//                                                Intent intent = new Intent(getApplicationContext(), DetailTask.class);
//                                                //send the Id
//                                                intent.putExtra("id", tasksListDatabase.get(position).getId());
//                                                startActivity(intent);
//                                            });
//                                            // set adapter on recycler view
//                                            recyclerView.setAdapter(customRecyclerViewAdapter);
//
//                                            // set other important properties
//                                            recyclerView.setHasFixedSize(true);
//                                            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//                                        });
                                        },
                                        error -> Log.e(TAG, error.toString(), error)
                                );
                            }
                        }
                    //}
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }
    private void getTasks() {
        //////////////////////////////////////////////
        /*
          Lab 32 AWS Amplify
          Fetch the Data From the API DynamoDB
         */
        if(teamName.equals("No Team setting")) {
            Amplify.API.query(
                    ModelQuery.list(Task.class),
                    success -> {
                        tasksListDatabase = new ArrayList<>();

                        if (success.hasData()) {
                            for (Task task : success.getData()) {
                                tasksListDatabase.add(task);
                            }
                        }

                        Log.i(TAG, "==== Task List1  ====" + tasksListDatabase);
                        // Send message to the handler to show the Tasks List in the Recycler View >>
                        Bundle bundle = new Bundle();
                        bundle.putString("tasksList", success.toString());

                        Message message = new Message();
                        message.setData(bundle);

                        handler.sendMessage(message);

//                    runOnUiThread(() -> {
//                        customRecyclerViewAdapter.setTasksList(tasksListDatabase);
//                    });
                    },
                    error -> Log.e(TAG, error.toString(), error)
            );
        }
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
    //Get the TeamName
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: called - The App is VISIBLE");
        super.onResume();
        getTeamTask();
        setUserNameTeamName();
        //authSession("onResume");
        authAttribute();
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
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Logout AWS
    private void logout() {
        Amplify.Auth.signOut(
                () -> {
                    Log.i(TAG, "Signed out successfully");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    authSession("logout");
                    finish();
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    private void navigateToSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
/////////////////////////////////////////////////////////////////

    private void setUserNameTeamName() {
        // get text out of shared preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // set text on text view User Name Received from the setting page
       // username.setText(sharedPreferences.getString(SettingsActivity.USERNAME, "No User Name setting"));

        teamName =  sharedPreferences.getString(SettingsActivity.TEAMNAME, "No Team setting");
        TextView textView = findViewById(R.id.team);
        textView.setText(teamName);
        Log.i(TAG, "==== Team name new   ====" + teamName);
    }

    /////////////////////////////////////////////////////////////////
//    private  void initialiseData(){
//        tasksList.add(new Task("Task1",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                Task.State.Complete));
//        tasksList.add(new Task("Task2",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                Task.State.New));
//        tasksList.add(new Task("Task3",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                Task.State.In_Progress));
//    }

    //////////////////////////////////////////////// Lab 33 ///////////////////////////////////////
    private  void initialTeams(){
//
        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    ArrayList<Team> teamsList = new ArrayList<>();
                    //if(response.hasData()) {
                        for (Team team : response.getData()) {
                            teamsList.add(team);
                        }
                        if (teamsList.size() < 3) {
                            for (int i = 1; i < 4; i++) {
                                Team team = Team.builder()
                                        .name("Team" + i)
                                        .build();
                                teamsListData.add(team);

                                Amplify.DataStore.save(team,
                                        success -> Log.i(TAG, "Saved item: " + success.item().getName()),
                                        error -> Log.e(TAG, "Could not save item to DataStore", error)
                                );

                                // API save to backend
                                Amplify.API.mutate(
                                        ModelMutation.create(team),
                                        success -> Log.i(TAG, "Saved item: " + success.getData().getName()),
                                        error -> Log.e(TAG, "Could not save item to API", error)
                                );
                            }
                        }
                 //   }
                    runOnUiThread(() -> {

                    });
                },
                error -> Log.e(TAG, "Query failure", error)
        );
//
    }

    ////////////////////////////////////////// Lab 36 ///////////////////////////////////
    // Get Auth Attribute :
    private void authAttribute(){
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i(TAG, "User attributes = " + attributes.get(2).getValue());
                    //  Send message to the handler to show the User name >>
                    Bundle bundle = new Bundle();
                    bundle.putString("name",  attributes.get(2).getValue());

                    Message message = new Message();
                    message.setData(bundle);

                    handler2.sendMessage(message);
                },
                error -> Log.e(TAG, "Failed to fetch user attributes.", error)
        );
    }
    // Get Auth Session :
    private void authSession(String method) {
        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i(TAG, "Auth Session => " + method +" "+ result.toString());
                    if(result.isSignedIn()){
                        //  Send message to the handler to show the Tasks List in the Recycler View >>
                        Bundle bundle = new Bundle();
                        bundle.putString("userName", "userName");

                        Message message = new Message();
                        message.setData(bundle);

                        handler2.sendMessage(message);
                    }
                },
                error -> Log.e(TAG, error.toString())
        );
    }
}