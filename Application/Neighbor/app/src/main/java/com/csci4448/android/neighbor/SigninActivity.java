package com.csci4448.android.neighbor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SigninActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private EditText passWordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        userNameEditText = (EditText) findViewById(R.id.username_for_login);
        passWordEditText = (EditText) findViewById(R.id.password_for_login);

        Button registerButton = (Button) findViewById(R.id.login_user_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String username = userNameEditText.getText().toString();
        String password = passWordEditText.getText().toString();

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in...");
        dialog.show();
        // Call the Parse login method
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    // Show the error message
                    Toast.makeText(SigninActivity.this, "Username or Password is Incorrect!", Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }


}
