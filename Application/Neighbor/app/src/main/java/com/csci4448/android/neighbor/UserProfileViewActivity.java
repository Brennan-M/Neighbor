package com.csci4448.android.neighbor;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class UserProfileViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);

        final String parseUserID;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            parseUserID = extras.getString("userID");
        } else {
            Toast.makeText(UserProfileViewActivity.this, "Something went wrong. Redirecting to previous page.", Toast.LENGTH_LONG).show();
            super.onBackPressed();
            parseUserID = "";
        }

        final TextView fullName = (TextView) findViewById(R.id.userNameTextView);
        final TextView userBio = (TextView) findViewById(R.id.userBioTextView);
        final TextView userLocation = (TextView) findViewById(R.id.userLocation);
        final ImageView userPicture = (ImageView) findViewById(R.id.userProfilePicture);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", parseUserID);
        // Execute the query to find the object with ID
        query.getInBackground(parseUserID, new GetCallback<ParseUser>() {
            public void done(final ParseUser user, ParseException e) {
                if (e == null) {
                    fullName.setText(user.getString("memberName"));
                    userBio.setText(user.getString("userBio"));
                    userLocation.setText(user.getString("location"));
                    ParseFile profilePictureParseFile = user.getParseFile("pictureFile");

                    if (profilePictureParseFile != null) {
                        try {
                            byte[] profilePicture;
                            profilePicture = profilePictureParseFile.getData();
                            userPicture.setImageBitmap(BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.length));
                        } catch (ParseException exp) {
                            Toast.makeText(UserProfileViewActivity.this, "Could not set profile picture", Toast.LENGTH_LONG);
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile_view, menu);
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
