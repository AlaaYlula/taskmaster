package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    public static final String TEAMNAME = "TeamName";
    private static final String TAG = "SettingsActivity";
    EditText mUsernameEdit;
    Spinner TeamSelector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button submitBtn = findViewById(R.id.submit);

        mUsernameEdit = findViewById(R.id.username);
        TeamSelector = findViewById(R.id.teams);

        ////////////////////// Team Spinner

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

                        // set adapter
                        TeamSelector.setAdapter(spinnerAdapterTeam);
                    });
                },
                error -> Log.e(TAG, "Query failure", error)
        );

        ////////////////////////////


        // On Click
        submitBtn.setOnClickListener(view -> {
            // Method to save the userName
            saveUserNameTeamName();
            // Method to save the TeamName
           // saveTeamName();

            // Check if no view has focus: https://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard-programmatically
            View view2 = this.getCurrentFocus();
            if (view2 != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }

            startActivity(new Intent(this,MainActivity.class));
        });
        // Enable the Button when write the userName in the Edit text
        mUsernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!submitBtn.isEnabled()) {
                    submitBtn.setEnabled(true);
                }

                if (editable.toString().length() == 0){
                    submitBtn.setEnabled(false);
                }
            }
        });
    }
    // Method to save the userName
    private void saveUserNameTeamName() {
        // get the text from the edit text
        String username = mUsernameEdit.getText().toString();

        // create shared preference object and set up an editor
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

        // save the text to shared preferences
        preferenceEditor.putString(USERNAME, username);
        preferenceEditor.apply();

        String teamName = TeamSelector.getSelectedItem().toString();
        preferenceEditor.putString(TEAMNAME,teamName);
        preferenceEditor.apply();

        Toast.makeText(this, "User Name Saved", Toast.LENGTH_SHORT).show();
    }


}