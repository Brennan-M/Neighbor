package com.csci4448.android.neighbor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

        NeighborUser neighbor = NeighborUser.getInstance();

        if (neighbor.getParseUser() != null) {
            Intent intent = new Intent(this, HomescreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, FrontscreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}