package com.csci4448.android.neighbor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FrontscreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontscreen);

        // Log in button click handler
        Button signinButton = (Button) findViewById(R.id.start_signin);

        signinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Starts an intent of the log in activity
                startActivity(new Intent(FrontscreenActivity.this, SigninActivity.class));
            }
        });

        // Sign up button click handler
        Button signupButton = (Button) findViewById(R.id.start_signup);

        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Starts an intent for the sign up activity
                startActivity(new Intent(FrontscreenActivity.this, SignupActivity.class));
            }
        });
    }
}
