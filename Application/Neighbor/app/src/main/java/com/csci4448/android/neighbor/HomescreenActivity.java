package com.csci4448.android.neighbor;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class HomescreenActivity extends AppCompatActivity {

    private EditText searchQuery;
    ParseUser currentUser;
    private byte[] profilePicture;
    ImageView userProfilePic;
    private TextView fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        currentUser = ParseUser.getCurrentUser();
        fullName = (TextView) findViewById(R.id.userName);
        userProfilePic = (ImageView) findViewById(R.id.profilePicture);

        fullName.setText(currentUser.getString("memberName"), TextView.BufferType.EDITABLE);
        ParseFile profilePictureParseFile = currentUser.getParseFile("pictureFile");

        if (profilePictureParseFile != null) {
            try {
                profilePicture = profilePictureParseFile.getData();
                userProfilePic.setImageBitmap(BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.length));
            } catch (ParseException e) {
                profilePicture = null;
            }
        }

        Button postItemButton = (Button) findViewById(R.id.post_item);
        postItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HomescreenActivity.this, PostItemActivity.class);
                startActivity(intent);
            }
        });

        searchQuery = (EditText) findViewById(R.id.search_item_edittext);

        Button searchQueryButton = (Button) findViewById(R.id.search_button);
        searchQueryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String searchingFor = searchQuery.getText().toString().trim();
                searchQuery.setText("");
                if (searchingFor.matches("")) {
                    Toast.makeText(HomescreenActivity.this, "Please specify an item to search for...", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(HomescreenActivity.this, SearchActivity.class);
                    intent.putExtra("Query", searchingFor);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homescreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            ParseUser.logOut();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.edit_profile) {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
