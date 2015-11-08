package com.csci4448.android.neighbor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * This is the main activity which runs upon the app starting. If the user is logged in,
 * they will be directed to the HomescreenActivity. Otherwise they will be directed
 * to the LoginActivity or the SignupActivity. Additionally, the connection
 * to the parse database is achieved here.
 */

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "8igWP6UKlYeVz0ROQPPaGJygz1AqMH3LcDZXWMNv", "qj077hg3oAFZbpmCeiomTTxEZxuWrNUFDZPR8alT");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, HomescreenActivity.class));
        } else {
            startActivity(new Intent(this, FrontscreenActivity.class));
        }
    }
}