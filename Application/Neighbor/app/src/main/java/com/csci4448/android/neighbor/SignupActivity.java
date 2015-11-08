package com.csci4448.android.neighbor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText newUsername;
    private EditText newEmail;
    private EditText newPassword;
    private EditText newPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        newUsername = (EditText) findViewById(R.id.new_username);
        newEmail = (EditText) findViewById(R.id.new_email);
        newPassword = (EditText) findViewById(R.id.new_password);
        newPasswordConfirmation = (EditText) findViewById(R.id.new_password_confirmation);

        Button registerNewUserButton = (Button) findViewById(R.id.register_new_user);
        registerNewUserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registerNewUser();
            }
        });
    }


    private void registerNewUser() {
        String username = newUsername.getText().toString().trim();
        String email = newEmail.getText().toString().trim();
        String password = newPassword.getText().toString().trim();
        String passwordDuplicate = newPasswordConfirmation.getText().toString().trim();

        boolean registrationError = false;

        StringBuilder registrationErrorMessage = new StringBuilder();

        if (username.length() == 0) {
            registrationError = true;
            registrationErrorMessage.append("Username cannot be blank!");
        }
        else if (email.length() == 0) {
            registrationError = true;
            registrationErrorMessage.append("Email cannot be blank!");
        }
        else if (email.length() < 5){
            // TODO: Check email validity and uniqueness
            registrationError = true;
            registrationErrorMessage.append("Enter a valid email address!");
        }
        else if (password.length() == 0) {
            registrationError = true;
            registrationErrorMessage.append("Password cannot be blank!");
        }
        else if (username.length() < 5) {
            registrationError = true;
            registrationErrorMessage.append("Username must be longer than 5 characters.");
        }
        else  if (password.length() < 5) {
            registrationError = true;
            registrationErrorMessage.append("Password must be longer than 5 characters.");
        }
        else if (!password.equals(passwordDuplicate)) {
            registrationError = true;
            registrationErrorMessage.append("Passwords do not match!");
        }

        if (registrationError) {
            Toast.makeText(SignupActivity.this, registrationErrorMessage.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Creating new user...");
        dialog.show();

        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);

        // Call the Parse signup method
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Route back to main activity which should then lead to our homescreen activity.
                    dialog.dismiss();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Error: Email Not Valid.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
