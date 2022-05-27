package com.example.taskmaster;

import android.app.Application;
import android.util.Log;
import android.view.View;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

public class taskMasterApplication extends Application {

    private static final String TAG = taskMasterApplication.class.getSimpleName() ;

    @Override
    public void onCreate() {
        super.onCreate();

        // Amplify Configure
        configureAmplify();
    }
/////////////////////////////////////////////////////// AWS Amplify ///////////////////////////

    private void configureAmplify() {
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e(TAG, "Could not initialize Amplify", e);
        }
    }
}
