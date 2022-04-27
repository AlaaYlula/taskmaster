package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    EditText mUsernameEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button submitBtn = findViewById(R.id.submit);

        mUsernameEdit = findViewById(R.id.username);

        // On Click
        submitBtn.setOnClickListener(view -> {
            // Method to save the userName
            saveUserName();

            // Check if no view has focus: https://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard-programmatically
            View view2 = this.getCurrentFocus();
            if (view2 != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }
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
    private void saveUserName() {
        // get the text from the edit text
        String username = mUsernameEdit.getText().toString();

        // create shared preference object and set up an editor
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

        // save the text to shared preferences
        preferenceEditor.putString(USERNAME, username);
        preferenceEditor.apply();

        Toast.makeText(this, "User Name Saved", Toast.LENGTH_SHORT).show();
    }


}