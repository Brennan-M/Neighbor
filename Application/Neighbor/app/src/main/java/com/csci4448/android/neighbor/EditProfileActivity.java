package com.csci4448.android.neighbor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditProfileActivity extends AppCompatActivity {


    private EditText fullName;
    private EditText location;
    private EditText userBio;

    // TODO: Could fill in the current bio as the text for the edittexts in the future.
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullName = (EditText) findViewById(R.id.fullname_edittext);
        location = (EditText) findViewById(R.id.location_edittext);
        userBio = (EditText) findViewById(R.id.user_bio_edittext);

        Button submitChangesButton = (Button) findViewById(R.id.submit_profile_changes);
        submitChangesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                updateProfileChanges();
            }
        });
    }

    private void updateProfileChanges() {

        String userLocation = location.getText().toString().trim();
        String userFullname = fullName.getText().toString().trim();
        String userBioInfo = userBio.getText().toString().trim();

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (!userLocation.matches("")) {
            currentUser.put("location", userLocation);
        }
        if (!userBioInfo.matches("")) {
            currentUser.put("userBio", userBioInfo);
        }
        if (!userFullname.matches("")) {
            currentUser.put("memberName", userFullname);
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile...");
        dialog.show();

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Route back to main activity which should then lead to our homescreen activity.
                    dialog.dismiss();
                    Intent intent = new Intent(EditProfileActivity.this, HomescreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Error: Email Not Valid.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            ParseUser.logOut();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
