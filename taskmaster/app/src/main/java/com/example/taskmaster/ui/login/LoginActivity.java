package com.example.taskmaster.ui.login;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.amplifyframework.core.Amplify;
import com.example.taskmaster.MainActivity;
import com.example.taskmaster.R;
import com.example.taskmaster.SignUpActivity;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add view : Build In Ui elements
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final TextView signUpPrompt = findViewById(R.id.sign_up_prompt);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        signUpPrompt.setOnClickListener(view -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                //usernameEditText.getText().toString(),
                //passwordEditText.getText().toString());
                loginButton.setEnabled(true);
            }
        };
//        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);


        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginButton.setEnabled(true);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Amplify.Auth.signIn(
                        email,
                        password,
                        result -> {
                            Log.i(TAG, result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        },
                        error -> {
                            Log.e(TAG, error.toString());
                            // Read https://www.codegrepper.com/code-examples/java/error+dialog+android
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (!isFinishing()){
                                        new AlertDialog.Builder(LoginActivity.this)
                                                .setTitle("Error")
                                                .setMessage(error.getRecoverySuggestion())
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Whatever...
                                                        startActivity(new Intent(LoginActivity.this,LoginActivity.class));
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