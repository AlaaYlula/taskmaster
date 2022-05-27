package com.example.taskmaster;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.example.taskmaster.ui.login.LoginActivity;

public class SignUpActivity extends AppCompatActivity {


    private static final String TAG =SignUpActivity.class.getSimpleName() ;
    public static final String EMAIL = "email";
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add view : Build In Ui elements
        setContentView(R.layout.activity_sign_up);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText nameEditText = findViewById(R.id.name);
        final TextView signInPrompt = findViewById(R.id.sign_in_prompt);
        final Button signupButton = findViewById(R.id.sign_up);
        loadingProgressBar = findViewById(R.id.loading);

        signInPrompt.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
                signupButton.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable s) {
                //usernameEditText.getText().toString(),
                //passwordEditText.getText().toString());
            }
        };
        //usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        // when finish typing
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //usernameEditText.getText().toString(),
                    // passwordEditText.getText().toString());
                    signupButton.setEnabled(true);
                }
                return false;
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString();

                // Sign Up :
                // add as many attributes as you wish
                AuthSignUpOptions options = AuthSignUpOptions.builder()
                        .userAttribute(AuthUserAttributeKey.email(), email)
                        .userAttribute(AuthUserAttributeKey.nickname(), name)
                        .build();

                Amplify.Auth.signUp(email, password, options,
                        result -> {
                            Log.i(TAG, "Result: " + result.toString());
                            loadingProgressBar.setVisibility(View.INVISIBLE);

                            Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
                            intent.putExtra(EMAIL, email);
                            startActivity(intent);

                            finish();
                        },
                        error -> {
                            Log.e(TAG, "Sign up failed", error);
                            // show a dialog of the error below
                            // error.getMessage()
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (!isFinishing()){
                                        new AlertDialog.Builder(SignUpActivity.this)
                                                .setTitle("Error")
                                                .setMessage(error.getMessage())
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Whatever...
                                                        startActivity(new Intent(SignUpActivity.this,SignUpActivity.class));
                                                    }
                                                }).show();
                                    }
                                }
                            });
                }
                );
            }

        });
    }
//
//    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }
//
//    private void showLoginFailed(@StringRes Integer errorString) {
//        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
//    }

}